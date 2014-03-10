package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.InteractionAction;
import com.cometproject.server.game.items.interactions.InteractionQueueItem;
import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.items.interactions.football.BallInteraction;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;

import java.util.List;

public class RollerInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
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
    public boolean onTick(RoomItem item) {
        if (!(item instanceof FloorItem)) {
            return false;
        }

        Position3D sqInfront = item.squareInfront();
        FloorItem floorItem = (FloorItem) item;

        List<GenericEntity> entitiesOnSq = floorItem.getRoom().getEntities().getEntitiesAt(floorItem.getX(), floorItem.getY());

        for (GenericEntity entity : entitiesOnSq) {
            // to-do: check valid position

            double toHeight = 0;

            for(FloorItem itemInStack : floorItem.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                toHeight += itemInStack.getDefinition().getHeight();
            }

            entity.updateAndSetPosition(new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight));
            floorItem.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(entity.getPosition(), new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight), floorItem.getId(), entity.getVirtualId(), 0));
        }

        List<FloorItem> itemsOnSq = floorItem.getRoom().getItems().getItemsOnSquare(item.getX(), item.getY());

        for(FloorItem itemOnSq : itemsOnSq) {
            if(itemOnSq.getId() == item.getId())
                continue;

            double toHeight = 0;

            for(FloorItem itemInStack : floorItem.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                toHeight += itemInStack.getDefinition().getHeight();
            }

            // Use the same logic footballs use to roll (no point re-creating it :P)
            //floorItem.getRoom().getEntities().broadcastMessage((itemOnSq, new Position3D(itemOnSq.getX(), itemOnSq.getY(), itemOnSq.getHeight()), new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight), itemOnSq.getRoom());
            floorItem.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(new Position3D(itemOnSq.getX(), itemOnSq.getY(), itemOnSq.getHeight()), new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight), floorItem.getId(), 0, itemOnSq.getId()));

            itemOnSq.setX(sqInfront.getX());
            itemOnSq.setY(sqInfront.getY());
            itemOnSq.setHeight((float) toHeight); // maybe / maybe not

            //itemOnSq.getRoom().getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(itemOnSq, itemOnSq.getOwner()));
            // TODO: save item position when finished rolling?
        }

        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
