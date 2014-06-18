package com.cometproject.server.game.rooms;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.filter.WordFilter;
import com.cometproject.server.game.rooms.models.types.StaticRoomModel;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.misc.ChatEmotionsManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import org.apache.solr.util.ConcurrentLRUCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoomManager {
    public static final Logger log = Logger.getLogger(RoomManager.class.getName());

    public static final int LRU_MAX_ENTRIES = Integer.parseInt(Comet.getServer().getConfig().getProperty("comet.game.rooms.data.max"));
    public static final int LRU_MAX_LOWER_WATERMARK = Integer.parseInt(Comet.getServer().getConfig().getProperty("comet.game.rooms.data.lowerWatermark"));

    private ConcurrentLRUCache<Integer, RoomData> roomDataInstances;

    private FastMap<Integer, Room> roomInstances;

    private Set<StaticRoomModel> models;
    private WordFilter filterManager;

    private RoomCycle globalCycle;
    private ChatEmotionsManager emotions;

    public RoomManager() {
        this.emotions = new ChatEmotionsManager();
        this.filterManager = new WordFilter();

        this.roomDataInstances = new ConcurrentLRUCache<>(LRU_MAX_ENTRIES, LRU_MAX_LOWER_WATERMARK);
        this.roomInstances = new FastMap<Integer, Room>().shared();

        this.globalCycle = new RoomCycle(Comet.getServer().getThreadManagement());

        this.loadModels();
    }

    private Room createRoomInstance(RoomData data) {
        if (data == null) {
            return null;
        }

        Room instance = new Room(data);

        // attributes
        instance.setAttribute("loadTime", System.currentTimeMillis());

        return instance;
    }


    public void removeInstance(int roomId) {
        if (!this.getRoomInstances().containsKey(roomId)) {
            return;
        }

        Room room = this.getRoomInstances().get(roomId);

        // Needs to check here also
        if (!room.needsRemoving()) {
            return;
        }

        if (!room.isDisposed()) {
            room.dispose();
        }

        this.getRoomInstances().remove(roomId);
    }

    public void removeData(int roomId) {
        if (!this.getRoomDataInstances().getMap().containsKey(roomId)) {
            return;
        }

        RoomData data = this.getRoomDataInstances().get(roomId);

        //clear the maps n shit
        data.dispose();

        this.roomDataInstances.remove(roomId);
    }

    public void loadModels() {
        try {
            if (this.models != null && this.getModels().size() != 0) {
                this.getModels().clear();
            }

            this.models = RoomDao.getModels();
        } catch (Exception e) {
            log.error("Error while loading room model", e);
        }

        log.info("Loaded " + this.getModels().size() + " room models");
    }

    public StaticRoomModel getModel(String id) {
        for (StaticRoomModel model : this.models) {
            if (model.getId().equals(id)) {
                return model;
            }
        }

        log.error("Couldn't find model: " + id);

        return null;
    }

    public Room get(int id) {
        if (this.getRoomInstances().containsKey(id)) {
           Room r = this.getRoomInstances().get(id);

            if (r.needsRemoving()) {
                this.getRoomInstances().remove(id);
                r.dispose();
            } else {
                r.unIdleIfRequired();
                return r;
            }
        }

        Room room = createRoomInstance(this.getRoomData(id));

        if (room == null) {
            log.warn("There was a problem loading room: " + id + ", data was null");
        }

        if (room != null) {
            this.roomInstances.put(room.getId(), room);
        }

        return room;
    }

    public RoomData getRoomData(int id) {
        if (this.getRoomDataInstances().getMap().containsKey(id)) {
            return this.getRoomDataInstances().get(id).setLastReferenced(Comet.getTime());
        }

        RoomData roomData = RoomDao.getRoomDataById(id);

        if (roomData != null) {
            this.getRoomDataInstances().put(id, roomData);
        }

        return roomData;
    }

    public void loadRoomsForUser(Player player) {
        player.getRooms().clear();

        Map<Integer, RoomData> rooms = RoomDao.getRoomsByPlayerId(player.getId());

        for (Map.Entry<Integer, RoomData> roomEntry : rooms.entrySet()) {
            player.getRooms().add(roomEntry.getKey());

            if (!this.roomDataInstances.getMap().containsKey(roomEntry.getKey())) {
                this.roomDataInstances.put(roomEntry.getKey(), roomEntry.getValue());
            }
        }
    }

    public List<RoomData> getRoomByQuery(String query) {
        ArrayList<RoomData> rooms = new ArrayList<>();

        List<RoomData> roomSearchResults = RoomDao.getRoomsByQuery(query);

        for (RoomData data : roomSearchResults) {
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

    public boolean isActive(int roomId) {
        Room room = this.roomInstances.get(roomId);

        return room != null && !room.isDisposed();
    }

    public int createRoom(String name, String model, Session client) {
        int roomId = RoomDao.createRoom(name, model, client.getPlayer().getId(), client.getPlayer().getData().getUsername());

        this.loadRoomsForUser(client.getPlayer());

        return roomId;
    }

    public List<RoomData> getRoomsByCategory(int category) {
        List<RoomData> rooms = new ArrayList<>();

        for (Room room : this.roomInstances.values()) {
            if (room == null || room.isDisposed() || (category != -1 && room.getData().getCategory().getId() != category)) {
                continue;
            }

            rooms.add(room.getData());
        }

        return rooms;
    }

    public ChatEmotionsManager getEmotions() {
        return this.emotions;
    }

    public FastMap<Integer, Room> getRoomInstances() {
        return this.roomInstances;
    }

    public ConcurrentLRUCache<Integer, RoomData> getRoomDataInstances() {
        return this.roomDataInstances;
    }

    public Set<StaticRoomModel> getModels() {
        return this.models;
    }

    public RoomCycle getGlobalCycle() {
        return this.globalCycle;
    }

    public WordFilter getFilter() {
        return filterManager;
    }
}
