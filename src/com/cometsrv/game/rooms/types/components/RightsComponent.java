package com.cometsrv.game.rooms.types.components;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.components.types.RoomBan;
import javolution.util.FastList;

import java.sql.ResultSet;
import java.util.List;

public class RightsComponent {
    private Room room;

    private List<Integer> rights;
    private List<RoomBan> bannedUsers;

    public RightsComponent(Room room) {
        this.room = room;

        this.rights = new FastList<>();
        this.bannedUsers = new FastList<>();

        loadRights();
    }

    public void dispose() {
        this.rights.clear();
        this.bannedUsers.clear();

        rights = null;
        bannedUsers = null;
        this.room = null;
    }

    public void loadRights() {
        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM room_rights WHERE room_id = " + this.room.getId());

            while(data.next()) {
                this.rights.add(data.getInt("player_id"));
            }
        } catch(Exception e) {
            this.room.log.error("Error while loading room rights", e);
        }
    }

    public boolean hasRights(int playerId) {
        if(this.room.getData().getOwnerId() == playerId) {
            return true;
        } else {
            return this.rights.contains(playerId);
        }
    }

    public void removeRights(int playerId) {
        this.rights.remove(rights.indexOf(playerId));
        Comet.getServer().getStorage().execute("DELETE from room_rights WHERE room_id = " + this.room.getId() + " AND player_id = " + playerId);
    }

    public void addRights(int playerId) {
        this.rights.add(playerId);
        Comet.getServer().getStorage().execute("INSERT into room_rights (`room_id`, `player_id`) VALUES(" + this.room.getId() + ", " + playerId + ");");
    }

    public void addBan(int userId) {
        this.bannedUsers.add(new RoomBan(userId));
    }

    public boolean hasBan(int userId) {
        for(RoomBan ban : this.bannedUsers) {
            if(ban.getId() == userId) {
                return true;
            }
        }

        return false;
    }

    public void cycle() {
        synchronized (this.bannedUsers) {
            for(RoomBan ban : this.bannedUsers) {
                if(ban.getCycle() >= 1) {
                    this.bannedUsers.remove(ban);
                    continue;
                }

                ban.increaseCycle();
            }
        }
    }

    public List<Integer> getAll() {
        return this.rights;
    }

    public Room getRoom() {
        return this.room;
    }
}
