package com.cometsrv.game.items.interactions.items;

import com.cometsrv.game.items.interactions.InteractionAction;
import com.cometsrv.game.items.interactions.InteractionQueueItem;
import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.items.RoomItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;

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
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar) {
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
            // to-do: check valid psoition

            double height = 1.0;

            for(FloorItem itemInStack : floorItem.getRoom().getItems().getItemsOnSquare(item.getX(), item.getY())) {
                height += itemInStack.getDefinition().getHeight();
            }

            entity.updateAndSetPosition(new Position3D(sqInfront.getX(), sqInfront.getY(), height));
            floorItem.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(entity.getPosition(), new Position3D(sqInfront.getX(), sqInfront.getY(), height), floorItem.getId(), entity.getVirtualId(), 0));
        }

        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
