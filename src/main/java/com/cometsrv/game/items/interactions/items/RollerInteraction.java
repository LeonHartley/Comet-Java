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

        if (av.getPathfinder() == null) { // Something wrong with pathfinder?
            return false;
        }

        if(!av.getPathfinder().checkSquare(sq.getX(), sq.getY())) {
            // Try again soon if they are still on the same square
            if (av.getPosition().getX() == floorItem.getX() && av.getPosition().getY() == floorItem.getY()) {
                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, null, 0, 10));
            }
            return false;
        }

        double height = 0.0;

        for(FloorItem itemInStack : floorItem.getRoom().getItems().getItemsOnSquare(item.getX(), item.getY())) {
            height += itemInStack.getDefinition().getHeight();
        }

        av.warpTo(sq.getX(), sq.getY());
        floorItem.getRoom().getAvatars().broadcast(SlideObjectBundleMessageComposer.compose(av.getPosition(), new Position(sq.getX(), sq.getY(), height), floorItem.getId(), av.getPlayer().getId(), 0));
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
