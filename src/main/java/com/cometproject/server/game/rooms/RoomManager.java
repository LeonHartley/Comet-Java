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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomManager {
    private FastMap<Integer, Room> rooms;
    private ArrayList<StaticRoomModel> models;
    private WordFilter filterManager;

    private RoomCycle globalProcessor;
    private ChatEmotionsManager emotions;

    public static Logger log = Logger.getLogger(RoomManager.class.getName());

    public RoomManager() {
        rooms = new FastMap<Integer, Room>().shared();
        models = new ArrayList<>();
        emotions = new ChatEmotionsManager();
        filterManager = new WordFilter();

        globalProcessor = new RoomCycle(Comet.getServer().getThreadManagement());

        loadModels();
    }

    public void loadModels() {
        try {
            if (this.getModels().size() != 0) {
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
        if (this.getRooms().containsKey(id)) {
            return this.getRooms().get(id);
        }

        try {
            Room room = RoomDao.getRoomById(id);

            if (room != null) {
                this.rooms.put(id, room);
                return room;
            }
        } catch (Exception e) {
            log.error("Error while loading room", e);
        }

        return null;
    }

    public void loadRoomsForUser(Player player) {
        try {
            player.getRooms().clear();

            Map<Integer, Room> rooms = RoomDao.getRoomsByPlayerId(player.getId());

            for(Map.Entry<Integer, Room> roomEntry : rooms.entrySet()) {
                player.getRooms().add(roomEntry.getKey());
                if(this.rooms.containsKey(roomEntry.getKey())) continue;

                this.rooms.put(roomEntry.getKey(), roomEntry.getValue());

            }
        } catch (Exception e) {
            log.error("Error while loading rooms for user", e);
        }
    }

    public List<Room> getRoomByQuery(String query) {
        ArrayList<Room> rooms = new ArrayList<>();

        try {
            List<RoomData> roomSearchResults = RoomDao.getRoomsByQuery(query);

            for(RoomData data : roomSearchResults) {
                if (this.getRooms().containsKey(data.getId())) {
                    rooms.add(this.getRooms().get(data.getId()));
                    continue;
                }

                Room room = new Room(data);

                this.getRooms().put(data.getId(), room);
                rooms.add(room);
            }

        } catch (Exception e) {
            log.error("Error while loading rooms by query", e);
        }

        if (rooms.size() == 0 && !query.toLowerCase().startsWith("owner:")) {
            return this.getRoomByQuery("owner:" + query);
        }

        return rooms;
    }

    public int createRoom(String name, String model, Session client) {
        int roomId = 0;
        try {
            roomId = RoomDao.createRoom(name, model, client.getPlayer().getId(), client.getPlayer().getData().getUsername());

            this.loadRoomsForUser(client.getPlayer());
        } catch (Exception e) {
            log.error("Error while creating a room", e);
        }

        return roomId;
    }

    public List<Room> getActiveRooms() {
        List<Room> rooms = new ArrayList<>();

        for (Room room : this.getRooms().values()) {
            if (room == null || room.getEntities() == null || room.getEntities().playerCount() < 1 || !room.isActive) {
                continue;
            }

            rooms.add(room);
        }

        return rooms;
    }

    public List<Room> getActiveRoomsByCategory(int category) {
        List<Room> rooms = new ArrayList<>();

        for (Room room : this.getRooms().values()) {
            if (room == null || room.getEntities() == null || room.getEntities().playerCount() < 1 || !room.isActive || (category != -1 && room.getData().getCategory().getId() != category)) {
                continue;
            }

            rooms.add(room);
        }

        return rooms;
    }

    public ChatEmotionsManager getEmotions() {
        return this.emotions;
    }

    public FastMap<Integer, Room> getRooms() {
        return this.rooms;
    }

    public List<StaticRoomModel> getModels() {
        return this.models;
    }

    public RoomCycle getGlobalProcessor() {
        return this.globalProcessor;
    }

    public WordFilter getFilter() {
        return filterManager;
    }
}
