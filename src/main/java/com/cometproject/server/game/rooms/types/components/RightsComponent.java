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
    private FastTable<RoomBan> bannedPlayers;

    public RightsComponent(Room room) {
        this.room = room;

        try {
            this.rights = RightsDao.getRightsByRoomId(room.getId());
        } catch (Exception e) {
            this.rights = new FastTable<Integer>().shared();
            this.room.log.error("Error while loading room rights", e);
        }

        this.bannedPlayers = new FastTable<RoomBan>().shared();
    }

    public void dispose() {
        this.rights.clear();
        this.bannedPlayers.clear();
    }

    public boolean hasRights(int playerId) {
        return (this.room.getData().getOwnerId() == playerId) || (this.rights.contains(playerId));
    }

    public void removeRights(int playerId) {
        if (this.rights.contains(playerId)) {
            this.rights.remove(rights.indexOf(playerId));
            RightsDao.delete(playerId, room.getId());
        }
    }

    public void addRights(int playerId) {
        this.rights.add(playerId);
        RightsDao.add(playerId, this.room.getId());
    }

    public void addBan(int playerId, String playerName, int length) {
        this.bannedPlayers.add(new RoomBan(playerId, playerName, length != -1 ? length * 2 : -1));
    }

    public boolean hasBan(int userId) {
        for (RoomBan ban : this.bannedPlayers) {
            if (ban.getPlayerId() == userId) {
                return true;
            }
        }

        return false;
    }

    public void removeBan(int playerId) {
        int indexToRemove = -1;

        for (RoomBan ban : this.bannedPlayers) {
            if (ban.getPlayerId() == playerId) {
                indexToRemove = this.bannedPlayers.indexOf(ban);
            }
        }

        if(indexToRemove != -1) {
            this.bannedPlayers.remove(indexToRemove);
        }
    }

    public void tick() {
        List<RoomBan> bansToRemove = new ArrayList<>();

        for (RoomBan ban : this.bannedPlayers) {
            if (ban.getTicksLeft() <= 0 && !ban.isPermanent()) {
                bansToRemove.add(ban);
            }

            ban.decreaseTicks();
        }

        for (RoomBan ban : bansToRemove) {
            this.bannedPlayers.remove(ban);
        }

        bansToRemove.clear();
    }

    public List<RoomBan> getBannedPlayers() {
        return this.bannedPlayers;
    }

    public List<Integer> getAll() {
        return this.rights;
    }

    public Room getRoom() {
        return this.room;
    }
}
