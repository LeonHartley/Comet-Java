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

    private FastMap<Integer, Room> loadedRoomInstances;
    private FastMap<Integer, Room> unloadingRoomInstances;

    private final Object syncObj = new Object();

    private Set<StaticRoomModel> models;
    private WordFilter filterManager;

    private RoomCycle globalCycle;
    private ChatEmotionsManager emotions;

    public RoomManager() {
        this.roomDataInstances = new ConcurrentLRUCache<>(LRU_MAX_ENTRIES, LRU_MAX_LOWER_WATERMARK);

        this.loadedRoomInstances = new FastMap<Integer, Room>().shared();
        this.unloadingRoomInstances = new FastMap<Integer, Room>().shared();

        this.emotions = new ChatEmotionsManager();
        this.filterManager = new WordFilter();

        this.globalCycle = new RoomCycle(Comet.getServer().getThreadManagement());

        this.loadModels();
        this.globalCycle.start();
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

        log.debug("Couldn't find model: " + id);

        return null;
    }

    /*private Room createRoomInstance(RoomData data) {
        if (data == null) { return null; }

        Room instance = new Room(data);

        // attributes
        instance.setAttribute("loadTime", System.currentTimeMillis());

        return instance;
    }*/

    /*public Room get(int id) {
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
            return null;
        }

        this.roomInstances.put(room.getId(), room);

        room.getItems().callOnLoad();

        return room;
    }*/

    public Room get(int id) {
        if(id == 0) return null;

        if (this.getRoomInstances().containsKey(id)) {
            return this.getRoomInstances().get(id);
        }

        synchronized (this.syncObj) {
            if (this.getRoomInstances().containsKey(id)) {
                return this.getRoomInstances().get(id);
            }

            RoomData data = this.getRoomData(id);

            if (data == null) {
                log.warn("There was a problem loading room: " + id + ", data was null");
                return null;
            }

//            try {
            Room room = new Room(data).load();
            this.loadedRoomInstances.put(id, room);

            this.finalizeRoomLoad(room);

            return room;
//            } finally {
//                this.finalizeRoomLoad(this.getRoomInstances().get(id));
//            }
        }
    }

    private void finalizeRoomLoad(Room room) {
        if (room == null) {
            return;
        }
        room.getItems().onLoaded();
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

    public void unloadIdleRooms() {
        for (Room room : this.unloadingRoomInstances.values()) {
            room.dispose();
        }

        this.unloadingRoomInstances.clear();

        synchronized (this.syncObj) {
            List<Room> idleRooms = new ArrayList<>();

            for (Room room : this.loadedRoomInstances.values()) {
                if (room.isIdle()) {
                    idleRooms.add(room);
                }
            }

            for (Room room : idleRooms) {
                this.loadedRoomInstances.remove(room.getId());
                this.unloadingRoomInstances.put(room.getId(), room);
            }
        }
    }

    public void forceUnload(int id) {
        if (this.loadedRoomInstances.containsKey(id)) {
            this.loadedRoomInstances.remove(id).dispose();
        }
    }

    /*public void removeInstance(int roomId) {
        if (!this.getRoomInstances().containsKey(roomId)) {
            return;
        }

        Room room = this.getRoomInstances().get(roomId);

        if (!room.isDisposed()) {
            room.dispose();
        }

        this.getRoomInstances().remove(roomId);
    }*/

    public void removeData(int roomId) {
        if (!this.getRoomDataInstances().getMap().containsKey(roomId)) {
            return;
        }

        this.getRoomInstances().remove(roomId);
    }

    public void loadRoomsForUser(Player player) {
        player.getRooms().clear();

        Map<Integer, RoomData> rooms = RoomDao.getRoomsByPlayerId(player.getId());

        for (Map.Entry<Integer, RoomData> roomEntry : rooms.entrySet()) {
            player.getRooms().add(roomEntry.getKey());

            if (!this.getRoomDataInstances().getMap().containsKey(roomEntry.getKey())) {
                this.getRoomDataInstances().put(roomEntry.getKey(), roomEntry.getValue());
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

    public boolean isActive(int id) {
        return this.getRoomInstances().containsKey(id);
    }

    public int createRoom(String name, String model, Session client) {
        int roomId = RoomDao.createRoom(name, model, client.getPlayer().getId(), client.getPlayer().getData().getUsername());

        this.loadRoomsForUser(client.getPlayer());

        return roomId;
    }

    public List<RoomData> getRoomsByCategory(int category) {
        List<RoomData> rooms = new ArrayList<>();

        for (Room room : this.getRoomInstances().values()) {
            if (category != -1 && room.getData().getCategory().getId() != category) {
                continue;
            }

            rooms.add(room.getData());
        }

        return rooms;
    }

    public final ChatEmotionsManager getEmotions() {
        return this.emotions;
    }

    public final FastMap<Integer, Room> getRoomInstances() {
        return this.loadedRoomInstances;
    }

    public final ConcurrentLRUCache<Integer, RoomData> getRoomDataInstances() {
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
}
