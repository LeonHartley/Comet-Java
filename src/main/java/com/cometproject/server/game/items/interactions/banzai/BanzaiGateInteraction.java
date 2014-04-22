package com.cometproject.server.game.items.interactions.banzai;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.avatars.effects.UserEffect;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;

public class BanzaiGateInteraction extends Interactor {

    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        if (!state)
            return false;

        Room room = avatar.getRoom();
        GameTeam team = GameTeam.valueOf(item.getDefinition().getInteraction().split("\\_")[1].toUpperCase());

        int id = avatar.getPlayer().getId();

        if (room.getGame().getInstance().getTeam(id).equals(team)) {
            room.getGame().getInstance().removeFromTeam(team, id);

            team = GameTeam.NONE;

            avatar.applyEffect(new UserEffect(team.getBanzaiEffect(), 0));
        } else {
            if (room.getGame().getInstance().isTeamed(id)) {
                GameTeam oldTeam = room.getGame().getInstance().getTeam(id);
                room.getGame().getInstance().removeFromTeam(oldTeam, id);

                for (FloorItem oldTeamGate : room.getItems().getByInteraction("bb_" + oldTeam.toString().toLowerCase() + "_gate")) {
                    oldTeamGate.setExtraData("" + room.getGame().getInstance().getTeams().get(oldTeam).size());
                    oldTeamGate.sendUpdate();
                }
            }

            room.getGame().getInstance().getTeams().get(team).add(id);
        }

        item.setExtraData("" + room.getGame().getInstance().getTeams().get(team).size());
        item.sendUpdate();

        avatar.applyEffect(new UserEffect(team.getBanzaiEffect(), 0));
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item, PlayerEntity avatar, int updateState) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
