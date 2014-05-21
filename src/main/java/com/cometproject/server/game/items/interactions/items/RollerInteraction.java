package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.InteractionAction;
import com.cometproject.server.game.items.interactions.InteractionQueueItem;
import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

import java.util.List;

public class RollerInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, GenericEntity avatar) {
        if (!(item instanceof FloorItem)) { // Rollers are always floor items
            return false;
        }

        if (state) {
            Room r = ((FloorItem) item).getRoom();
            int speed = r.hasAttribute("setspeed") ? (int)r.getAttribute("setspeed") : 9;

            item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 0, speed));
            return true;
        }

        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, GenericEntity avatar) {
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
        if (!(item instanceof FloorItem)) {
            return false;
        }

        Position3D sqInfront = item.squareInfront();
        FloorItem floorItem = (FloorItem) item;

        if (!floorItem.getRoom().getMapping().isValidPosition(sqInfront))
            return false;

        List<GenericEntity> entitiesOnSq = floorItem.getRoom().getEntities().getEntitiesAt(floorItem.getX(), floorItem.getY());

        for (GenericEntity entity : entitiesOnSq) {
            if (!entity.getRoom().getMapping().isValidStep(entity.getPosition(), sqInfront, true) || !entity.getRoom().getEntities().isSquareAvailable(sqInfront.getX(), sqInfront.getY())) {
                continue;
            }

            double toHeight = 0;

            for (FloorItem itemInStack : floorItem.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                if(!itemInStack.getDefinition().canSit)
                    toHeight += itemInStack.getDefinition().getHeight();
            }

            entity.updateAndSetPosition(new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight));
            floorItem.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(entity.getPosition(), new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight), floorItem.getId(), entity.getVirtualId(), 0));
        }

        List<FloorItem> itemsOnSq = floorItem.getRoom().getItems().getItemsOnSquare(item.getX(), item.getY());

        for (FloorItem itemOnSq : itemsOnSq) {
            if (itemOnSq.getId() == item.getId())
                continue;

            double toHeight = 0;
            boolean needsSave = true;
            boolean needsCancel = false;

            for (FloorItem itemInStack : floorItem.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                if (!itemInStack.getDefinition().canStack) {
                    needsCancel = true;
                }

                if (needsSave && itemInStack.getDefinition().getInteraction().equals("roller"))
                    needsSave = false;

                toHeight += itemInStack.getDefinition().getHeight();
            }

            if(toHeight > 0 && needsSave)
                toHeight = 0;

            if(itemOnSq.getHeight() < ((FloorItem) item).getHeight()) {
                continue;
            }

            if (needsCancel)
                continue;

            floorItem.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(new Position3D(itemOnSq.getX(), itemOnSq.getY(), itemOnSq.getHeight()), new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight), floorItem.getId(), 0, itemOnSq.getId()));

            itemOnSq.setX(sqInfront.getX());
            itemOnSq.setY(sqInfront.getY());
            itemOnSq.setHeight((float) toHeight);

            if (needsSave) {
                RoomItemDao.saveItemPosition(itemOnSq.getX(), itemOnSq.getY(), itemOnSq.getHeight(), itemOnSq.getRotation(), itemOnSq.getId());
            }
        }

        if (itemsOnSq.size() > 0) {
            floorItem.getRoom().getMapping().getTile(sqInfront.getX(), sqInfront.getY()).reload();
            floorItem.getRoom().getMapping().getTile(floorItem.getX(), floorItem.getY()).reload();
        }

        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
