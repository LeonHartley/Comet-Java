package com.cometproject.server.game.rooms;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.cache.CometCache;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.avatars.misc.FilterManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomModel;
import com.cometproject.server.game.rooms.types.misc.ChatEmotionsManager;
import com.cometproject.server.network.sessions.Session;
import java.util.ArrayList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    private FastMap<Integer, Room> rooms;
    private ArrayList<RoomModel> models;
    private FilterManager filterManager;

    private RoomCycle globalProcessor;
    private ChatEmotionsManager emotions;

    public static Logger log = Logger.getLogger(RoomManager.class.getName());

    public RoomManager() {
        rooms = new FastMap<Integer, Room>().atomic();
        models = new ArrayList<>();
        emotions = new ChatEmotionsManager();
        filterManager = new FilterManager();

        globalProcessor = new RoomCycle(Comet.getServer().getThreadManagement());

        loadModels();
    }

    public void loadModels() {
        try {
            if (this.getModels().size() != 0) {
                this.getModels().clear();
            }

            ResultSet result = Comet.getServer().getStorage().getTable("SELECT * FROM room_models");

            while (result.next()) {
                this.getModels().add(new RoomModel(result));
            }
        } catch (Exception e) {
            log.error("Error while loading room model", e);
        }

        log.info("Loaded " + this.getModels().size() + " room models");
    }

    public RoomModel getModel(String id) {
        for (RoomModel model : this.models) {
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
            ResultSet room = Comet.getServer().getStorage().getRow("SELECT * FROM rooms WHERE id = " + id);

            if (room != null) {
                Room newRoom = new Room(new RoomData(room));
                this.rooms.put(id, newRoom);

                return newRoom;
            }
        } catch (Exception e) {
            log.error("Error while loading room", e);
        }

        return null;
    }

    public void loadRoomsForUser(Player player) {
        try {
            ResultSet room = Comet.getServer().getStorage().getTable("SELECT * FROM rooms WHERE owner_id = " + player.getId() + " ORDER by id ASC");

            while (room.next()) {
                RoomData data = new RoomData(room);

                if (this.getRooms().containsKey(data.getId())) {
                    player.getRooms().put(data.getId(), this.getRooms().get(data.getId()));
                    continue;
                }

                Room r = new Room(data);
                this.getRooms().put(data.getId(), r);
                player.getRooms().put(r.getId(), r);
            }
        } catch (Exception e) {
            log.error("Error while loading rooms for user", e);
        }
    }

    public List<Room> getRoomByQuery(String query) {
        ArrayList<Room> rooms = new ArrayList<>();

        try {
            PreparedStatement std;

            if (query.startsWith("owner:")) {
                std = Comet.getServer().getStorage().prepare("SELECT * FROM rooms WHERE owner = ?");
                std.setString(1, query.split("owner:")[1]);
            } else {
                std = Comet.getServer().getStorage().prepare("SELECT * FROM rooms WHERE name LIKE ? LIMIT 150");
                std.setString(1, "%" + query + "%");
            }

            ResultSet room = std.executeQuery();

            while (room.next()) {
                RoomData data = new RoomData(room);

                if (this.getRooms().containsKey(data.getId())) {
                    rooms.add(this.getRooms().get(data.getId()));
                    continue;
                }

                Room r = new Room(data);

                this.getRooms().put(data.getId(), r);
                rooms.add(r);
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
            PreparedStatement std = Comet.getServer().getStorage().prepare("INSERT into rooms (`owner_id`, `owner`, `name`, `model`) VALUES(?, ?, ?, ?);", true);

            std.setInt(1, client.getPlayer().getId());
            std.setString(2, client.getPlayer().getData().getUsername());
            std.setString(3, name);
            std.setString(4, model);

            std.execute();

            ResultSet result = std.getGeneratedKeys();

            if (result.next()) {
                roomId = result.getInt(1);

                this.loadRoomsForUser(client.getPlayer());
            }
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

    public ChatEmotionsManager getEmotions() {
        return this.emotions;
    }

    public FastMap<Integer, Room> getRooms() {
        return this.rooms;
    }

    public List<RoomModel> getModels() {
        return this.models;
    }

    public RoomCycle getGlobalProcessor() {
        return this.globalProcessor;
    }

    public FilterManager getFilter() {
        return filterManager;
    }
}
