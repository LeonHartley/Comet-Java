package com.cometproject.server.game.items.interactions.banzai;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.banzai.BanzaiGame;

public class BanzaiPatchInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, GenericEntity avatar) {
        if (!state)
            return false;


        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, GenericEntity avatar) {
        if(!(avatar instanceof PlayerEntity))
            return false;

        if (avatar.getRoom().getGame().getInstance() != null && avatar.getRoom().getGame().getInstance().isTeamed(((PlayerEntity) avatar).getPlayerId())) {
            ((BanzaiGame) avatar.getRoom().getGame().getInstance()).captureTile(item.getX(), item.getY(), ((BanzaiGame) avatar.getRoom().getGame().getInstance()).getTeam(((PlayerEntity) avatar).getPlayerId()));

            avatar.getRoom().log.debug("Tile captured! x: " + item.getX() + ", y: " + item.getY());
        }

        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, GenericEntity avatar, boolean isWiredTriggered) {
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
    public boolean onTick(RoomItem item, GenericEntity avatar, int updateState) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
