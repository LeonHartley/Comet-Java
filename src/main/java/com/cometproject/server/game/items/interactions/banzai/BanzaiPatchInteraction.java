package com.cometproject.server.game.items.interactions.banzai;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;

public class BanzaiPatchInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {

        /*if(avatar.isTeamed()) {
            if(avatar.getRoom().getGame().getInstance() != null) {
                ((BanzaiGame)avatar.getRoom().getGame().getInstance()).captureTile(item.getX(), item.getY(), avatar.getTeam());

                avatar.getRoom().log.debug("Tile captured! x: " + item.getX() + ", y: " + item.getY());
            }
        }*/

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
    public boolean onTick(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
