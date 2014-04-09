package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.InteractionAction;
import com.cometproject.server.game.items.interactions.InteractionQueueItem;
import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;

public class OneWayGateInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        FloorItem floorItem = (FloorItem) item;
        Position3D doorPosition = new Position3D(floorItem.getX(), floorItem.getY());

        if(doorPosition.squareInFront(item.getRotation()).getX() != avatar.getPosition().getX() && doorPosition.squareInFront(item.getRotation()).getY() != avatar.getPosition().getY()) {
            avatar.moveTo(doorPosition.squareInFront(item.getRotation()).getX(), doorPosition.squareInFront(item.getRotation()).getY());
            return false;
        }

        avatar.setOverriden(true);
        avatar.moveTo(doorPosition.squareBehind(item.getRotation()).getX(), doorPosition.squareBehind(item.getRotation()).getY());

        item.setExtraData("1");
        item.sendUpdate();

        item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 0, 10));
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
        item.setExtraData("0");
        item.sendUpdate();

        avatar.setOverriden(false);
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
