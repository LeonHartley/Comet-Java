package com.cometsrv.game.items.interactions.items;

import com.cometsrv.game.items.interactions.InteractionAction;
import com.cometsrv.game.items.interactions.InteractionQueueItem;
import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.avatars.misc.Position;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.items.RoomItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;

public class RollerInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, Avatar avatar) {
        if (!(item instanceof FloorItem)) { // Rollers are always floor items
            return false;
        }

        if (state) {
            item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 0, 10));
            return true;
        }

        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item) {
        if (!(item instanceof FloorItem)) {
            return false;
        }

        Position sq = item.squareInfront();

        FloorItem floorItem = (FloorItem) item;
        Avatar av = floorItem.getRoom().getAvatars().getAvatarAt(floorItem.getX(), floorItem.getY());

        if(av.getPathfinder() == null || !av.getPathfinder().checkSquare(sq.getX(), sq.getY())) {
            return false;
        }

        floorItem.getRoom().getAvatars().broadcast(SlideObjectBundleMessageComposer.compose(av.getPosition(), new Position(sq.getX(), sq.getY(), floorItem.getHeight()), floorItem.getId(), av.getPlayer().getId(), 0));
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
