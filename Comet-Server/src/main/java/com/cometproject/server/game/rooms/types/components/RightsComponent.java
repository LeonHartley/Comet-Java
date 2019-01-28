package com.cometproject.server.game.rooms.types.components;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.types.RoomBan;
import com.cometproject.server.game.rooms.types.components.types.RoomMute;
import com.cometproject.server.network.ws.messages.alerts.MutedMessage;
import com.cometproject.server.storage.queries.rooms.RightsDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class RightsComponent {
    private Room room;

    private List<Integer> rights;
    private Map<Integer, RoomBan> bannedPlayers;
    private Map<Integer, RoomMute> mutedPlayers;

    public RightsComponent(Room room) {
        this.room = room;

        try {
            if (room.getCachedData() != null) {
                this.rights = room.getCachedData().getRights();
            } else {
                this.rights = RightsDao.getRightsByRoomId(room.getId());
            }
        } catch (Exception e) {
            this.rights = new CopyOnWriteArrayList<>();
            this.room.log.error("Error while loading room rights", e);
        }

        this.bannedPlayers = RightsDao.getRoomBansByRoomId(this.room.getId());
        this.mutedPlayers = new ConcurrentHashMap<>();
    }

    public void dispose() {
        this.rights.clear();
        this.bannedPlayers.clear();
    }

    public boolean hasRights(int playerId) {
        return this.hasRights(playerId, true);
    }

    public boolean hasRights(int playerId, boolean checkGroup) {
        if (checkGroup && checkGroupRights(playerId)) return true;

        return this.room.getData().getOwnerId() == playerId || this.rights.contains(playerId);
    }

    private boolean checkGroupRights(int playerId) {
        final IGroup group = this.getRoom().getGroup();

        if (group != null && group.getData() != null && group.getMembers() != null && group.getMembers().getAll() != null) {
            if (group.getData().canMembersDecorate() && group.getMembers().getAll().containsKey(playerId)) {
                return true;
            }

            return group.getMembers().getAdministrators().contains(playerId);
        }
        return false;
    }

    public boolean canPlaceFurniture(final int playerId) {
        if (checkGroupRights(playerId)) return true;

        if (this.hasRights(playerId, false) && CometSettings.playerRightsItemPlacement) {
            return true;
        }

        return this.room.getData().getOwnerId() == playerId;
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

    public void addBan(int playerId, String playerName, int expireTimestamp) {
        this.bannedPlayers.put(playerId, new RoomBan(playerId, playerName, expireTimestamp));
        RightsDao.addRoomBan(playerId, this.room.getId(), expireTimestamp);
    }

    public void addMute(int playerId, int minutes) {
        final PlayerEntity playerEntity = this.getRoom().getEntities().getEntityByPlayerId(playerId);
        if (playerEntity != null) {
            if (playerEntity.getPlayer().getSession().getWsChannel() != null) {
                playerEntity.getPlayer().getSession().sendWs(new MutedMessage(MutedMessage.MuteType.USER_MUTE, true, null));
            }
        }

        this.mutedPlayers.put(playerId, new RoomMute(playerId, (minutes * 60) * 2));
    }

    public boolean hasBan(int userId) {
        return this.bannedPlayers.containsKey(userId);
    }

    public void removeBan(int playerId) {
        this.bannedPlayers.remove(playerId);

        // delete it from the db.
        RightsDao.deleteRoomBan(playerId, this.room.getId());
    }

    public boolean hasMute(int playerId) {
        return this.mutedPlayers.containsKey(playerId);
    }

    public int getMuteTime(int playerId) {
        if (this.hasMute(playerId)) {
            return this.mutedPlayers.get(playerId).getTicksLeft() / 2;
        }

        return 0;
    }

    public void tick() {
        List<RoomBan> bansToRemove = new ArrayList<>();
        List<RoomMute> mutesToRemove = new ArrayList<>();

        for (RoomBan ban : this.bannedPlayers.values()) {
            if (ban.getExpireTimestamp() <= Comet.getTime() && !ban.isPermanent()) {
                bansToRemove.add(ban);
            }
        }

        for (RoomMute mute : this.mutedPlayers.values()) {
            if (mute.getTicksLeft() <= 0) {
                final PlayerEntity entity = this.getRoom().getEntities().getEntityByPlayerId(mute.getPlayerId());

                if (entity != null) {
                    if (entity.getPlayer().getSession().getWsChannel() != null) {
                        entity.getPlayer().getSession().sendWs(new MutedMessage(MutedMessage.MuteType.USER_MUTE, false, null));
                    }
                }

                mutesToRemove.add(mute);
            }

            mute.decreaseTicks();
        }


        for (RoomBan ban : bansToRemove) {
            this.bannedPlayers.remove(ban.getPlayerId());
        }

        for (RoomMute mute : mutesToRemove) {
            this.mutedPlayers.remove(mute.getPlayerId());
        }

        bansToRemove.clear();
        mutesToRemove.clear();
    }

    public Map<Integer, RoomBan> getBannedPlayers() {
        return this.bannedPlayers;
    }

    public List<Integer> getAll() {
        return this.rights;
    }

    public Room getRoom() {
        return this.room;
    }
}
