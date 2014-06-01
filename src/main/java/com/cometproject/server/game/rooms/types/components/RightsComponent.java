package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.types.RoomBan;
import com.cometproject.server.storage.queries.rooms.RightsDao;
import javolution.util.FastTable;

import java.util.ArrayList;
import java.util.List;

public class RightsComponent {
    private Room room;

    private FastTable<Integer> rights;
    private FastTable<RoomBan> bannedUsers;

    public RightsComponent(Room room) {
        this.room = room;

        try {
            this.rights = RightsDao.getRightsByRoomId(room.getId());
        } catch (Exception e) {
            this.rights = new FastTable<Integer>().shared();
            this.room.log.error("Error while loading room rights", e);
        }

        this.bannedUsers = new FastTable<RoomBan>().shared();
    }

    public void dispose() {
        this.rights.clear();
        this.bannedUsers.clear();

        this.room = null;
    }

    public boolean hasRights(int playerId) {
        return this.room.getData().getOwnerId() == playerId || this.rights.contains(playerId);
    }

    public void removeRights(int playerId) {
        if(this.rights.contains(playerId)) {
            this.rights.remove(rights.indexOf(playerId));
            RightsDao.delete(playerId, room.getId());
        }
    }

    public void addRights(int playerId) {
        this.rights.add(playerId);
        RightsDao.add(playerId, this.room.getId());
    }

    public void addBan(int userId) {
        this.bannedUsers.add(new RoomBan(userId));
    }

    public boolean hasBan(int userId) {
        for (RoomBan ban : this.bannedUsers) {
            if (ban.getId() == userId) {
                return true;
            }
        }

        return false;
    }

    public void cycle() {
        for (RoomBan ban : this.bannedUsers) {
            if (ban.getCycle() >= 1) {
                this.bannedUsers.remove(ban);
                continue;
            }

            ban.increaseCycle();
        }
    }

    public List<Integer> getAll() {
        return this.rights;
    }

    public Room getRoom() {
        return this.room;
    }
}
