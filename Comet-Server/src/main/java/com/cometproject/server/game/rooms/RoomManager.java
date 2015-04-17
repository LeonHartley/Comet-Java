package com.cometproject.server.game.rooms;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.filter.WordFilter;
import com.cometproject.server.game.rooms.models.types.StaticRoomModel;
import com.cometproject.server.game.rooms.types.RoomDataInstance;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.game.rooms.types.misc.ChatEmotionsManager;
import com.cometproject.api.game.rooms.settings.RoomTradeState;
import com.cometproject.server.network.messages.outgoing.room.events.RoomPromotionMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomDao;
import com.cometproject.server.utilities.Initializable;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import org.apache.solr.util.ConcurrentLRUCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class RoomManager implements Initializable {
    private static RoomManager roomManagerInstance;
    public static final Logger log = Logger.getLogger(RoomManager.class.getName());

    public static final int LRU_MAX_ENTRIES = Integer.parseInt(Comet.getServer().getConfig().getProperty("comet.game.rooms.data.max"));
    public static final int LRU_MAX_LOWER_WATERMARK = Integer.parseInt(Comet.getServer().getConfig().getProperty("comet.game.rooms.data.lowerWatermark"));

    private ConcurrentLRUCache<Integer, RoomDataInstance> roomDataInstances;

    private Map<Integer, RoomInstance> loadedRoomInstances;
    private Map<Integer, RoomInstance> unloadingRoomInstances;

    private Map<Integer, RoomPromotion> roomPromotions;

    private Set<StaticRoomModel> models;
    private WordFilter filterManager;

    private RoomCycle globalCycle;
    private ChatEmotionsManager emotions;

    public RoomManager() {

    }

    @Override
    public void initialize() {
        this.roomDataInstances = new ConcurrentLRUCache<>(LRU_MAX_ENTRIES, LRU_MAX_LOWER_WATERMARK);

        this.loadedRoomInstances = new FastMap<Integer, RoomInstance>().shared();
        this.unloadingRoomInstances = new FastMap<Integer, RoomInstance>().shared();
        this.roomPromotions = new ConcurrentHashMap<>();

        this.emotions = new ChatEmotionsManager();
        this.filterManager = new WordFilter();

        this.globalCycle = new RoomCycle();

        this.loadPromotedRooms();
        this.loadModels();

        this.globalCycle.start();

        log.info("RoomManager initialized");
    }

    public static RoomManager getInstance() {
        if (roomManagerInstance == null)
            roomManagerInstance = new RoomManager();

        return roomManagerInstance;
    }

    public void loadPromotedRooms() {
        RoomDao.deleteExpiredRoomPromotions();
        RoomDao.getActivePromotions(this.roomPromotions);

        log.info("Loaded " + this.getRoomPromotions().size() + " room promotions");
    }

    public void loadModels() {
        if (this.models != null && this.getModels().size() != 0) {
            this.getModels().clear();
        }

        this.models = RoomDao.getModels();

        log.info("Loaded " + this.getModels().size() + " room models");
    }

    public StaticRoomModel getModel(String id) {
        for (StaticRoomModel model : this.models) {
            if (model.getId().equals(id)) {
                return model;
            }
        }

        log.debug("Couldn't find model: " + id);

        return null;
    }

    public RoomInstance get(int id) {
        if (id == 0) return null;

        if (this.getRoomInstances().containsKey(id)) {
            return this.getRoomInstances().get(id);
        }

        if (this.getRoomInstances().containsKey(id)) {
            return this.getRoomInstances().get(id);
        }

        RoomDataInstance data = this.getRoomData(id);

        if (data == null) {
            return null;
        }

        RoomInstance room = new RoomInstance(data).load();

        if (room == null) return null;

        this.loadedRoomInstances.put(id, room);

        this.finalizeRoomLoad(room);

        return room;
    }

    private void finalizeRoomLoad(RoomInstance room) {
        if (room == null) {
            return;
        }
        room.getItems().onLoaded();
    }

    public RoomDataInstance getRoomData(int id) {
        if (this.getRoomDataInstances().getMap().containsKey(id)) {
            return this.getRoomDataInstances().get(id).setLastReferenced(Comet.getTime());
        }

        RoomDataInstance roomData = RoomDao.getRoomDataById(id);

        if (roomData != null) {
            this.getRoomDataInstances().put(id, roomData);
        }

        return roomData;
    }

    public void unloadIdleRooms() {
        for (RoomInstance room : this.unloadingRoomInstances.values()) {
            room.dispose();
        }

        this.unloadingRoomInstances.clear();

        List<RoomInstance> idleRooms = new ArrayList<>();

        for (RoomInstance room : this.loadedRoomInstances.values()) {
            if (room.isIdle()) {
                idleRooms.add(room);
            }
        }

        for (RoomInstance room : idleRooms) {
            this.loadedRoomInstances.remove(room.getId());
            this.unloadingRoomInstances.put(room.getId(), room);
        }
    }

    public void forceUnload(int id) {
        if (this.loadedRoomInstances.containsKey(id)) {
            this.loadedRoomInstances.remove(id).dispose();
        }
    }

    public void removeData(int roomId) {
        if (!this.getRoomDataInstances().getMap().containsKey(roomId)) {
            return;
        }

        this.getRoomInstances().remove(roomId);
    }

    public void loadRoomsForUser(Player player) {
        player.getRooms().clear();

        Map<Integer, RoomDataInstance> rooms = RoomDao.getRoomsByPlayerId(player.getId());

        for (Map.Entry<Integer, RoomDataInstance> roomEntry : rooms.entrySet()) {
            player.getRooms().add(roomEntry.getKey());

            if (!this.getRoomDataInstances().getMap().containsKey(roomEntry.getKey())) {
                this.getRoomDataInstances().put(roomEntry.getKey(), roomEntry.getValue());
            }
        }
    }

    public List<RoomDataInstance> getRoomByQuery(String query) {
        ArrayList<RoomDataInstance> rooms = new ArrayList<>();

        if (query.equals("tag:")) return rooms;

        List<RoomDataInstance> roomSearchResults = RoomDao.getRoomsByQuery(query);

        for (RoomDataInstance data : roomSearchResults) {
            if (!this.getRoomDataInstances().getMap().containsKey(data.getId())) {
                this.getRoomDataInstances().put(data.getId(), data);
            }

            rooms.add(data);
        }

        if (rooms.size() == 0 && !query.toLowerCase().startsWith("owner:")) {
            return this.getRoomByQuery("owner:" + query);
        }

        return rooms;
    }

    public boolean isActive(int id) {
        return this.getRoomInstances().containsKey(id);
    }

    public int createRoom(String name, String description, String model, int category, int maxVisitors, int tradeState, Session client) {
        int roomId = RoomDao.createRoom(name, model, description, category, maxVisitors, RoomTradeState.valueOf(tradeState), client.getPlayer().getId(), client.getPlayer().getData().getUsername());

        this.loadRoomsForUser(client.getPlayer());

        return roomId;
    }

    public List<RoomDataInstance> getRoomsByCategory(int category) {
        List<RoomDataInstance> rooms = new ArrayList<>();

        for (RoomInstance room : this.getRoomInstances().values()) {
            if (category != -1 && room.getData().getCategory().getId() != category) {
                continue;
            }

            rooms.add(room.getData());
        }

        return rooms;
    }

    public void promoteRoom(int roomId, String name, String description) {
        if (this.roomPromotions.containsKey(roomId)) {
            RoomPromotion promo = this.roomPromotions.get(roomId);
            promo.setTimestampFinish(promo.getTimestampFinish() + (RoomPromotion.DEFAULT_PROMO_LENGTH * 60));

            RoomDao.updatePromotedRoom(promo);
        } else {
            RoomPromotion roomPromotion = new RoomPromotion(roomId, name, description);
            RoomDao.createPromotedRoom(roomPromotion);

            this.roomPromotions.put(roomId, roomPromotion);
        }

        if (this.get(roomId) != null) {
            RoomInstance room = this.get(roomId);

            if (room.getEntities() != null && room.getEntities().realPlayerCount() >= 1) {
                room.getEntities().broadcastMessage(new RoomPromotionMessageComposer(room.getData(), this.roomPromotions.get(roomId)));
            }
        }
    }

    public boolean hasPromotion(int roomId) {
        if (this.roomPromotions.containsKey(roomId) && !this.roomPromotions.get(roomId).isExpired()) {
            return true;
        }

        return false;
    }

    public final ChatEmotionsManager getEmotions() {
        return this.emotions;
    }

    public final Map<Integer, RoomInstance> getRoomInstances() {
        return this.loadedRoomInstances;
    }

    public final ConcurrentLRUCache<Integer, RoomDataInstance> getRoomDataInstances() {
        return this.roomDataInstances;
    }

    public final Set<StaticRoomModel> getModels() {
        return this.models;
    }

    public final RoomCycle getGlobalCycle() {
        return this.globalCycle;
    }

    public final WordFilter getFilter() {
        return filterManager;
    }

    public Map<Integer, RoomPromotion> getRoomPromotions() {
        return roomPromotions;
    }
}
