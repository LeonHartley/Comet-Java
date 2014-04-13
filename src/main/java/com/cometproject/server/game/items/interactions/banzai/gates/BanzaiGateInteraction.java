package com.cometproject.server.game.items.interactions.banzai.gates;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;

public class BanzaiGateInteraction extends Interactor {

    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        if(!state)
            return false;

        Room room = avatar.getRoom();
        GameTeam team = GameTeam.valueOf(item.getDefinition().getInteraction().split("\\_")[1].toUpperCase());

        if(room.getGame().getInstance() == null) {
            room.getGame().createNew(GameType.BANZAI);

            if(Comet.isDebugging)
                room.getGame().getInstance().startTimer(120);
        }

        if(room.getGame().getInstance().getType() != GameType.BANZAI) {
            return false;
        }

        int id = avatar.getPlayer().getId();

        if(room.getGame().getInstance().isTeamed(id)) {
            room.getGame().getInstance().removeFromTeam(id);
        }

        int teamTotal = Integer.parseInt(item.getExtraData()) + 1;

        item.setExtraData("" + teamTotal);
        item.sendUpdate();

        room.getGame().getInstance().getTeams().put(id, team);
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
