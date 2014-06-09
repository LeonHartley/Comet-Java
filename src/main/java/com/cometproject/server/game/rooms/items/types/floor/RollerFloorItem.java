package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import org.apache.log4j.varia.Roller;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RollerFloorItem extends RoomItemFloor {
    private List<RoomItemFloor> interactingItems = new ArrayList<>();

    public RollerFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onItemStacked(List<RoomItemFloor> itemsInStack) {
        for (RoomItemFloor fl : itemsInStack) {
            if (!(fl instanceof RollerFloorItem)) {
                if (!interactingItems.contains(fl)) {
                    interactingItems.add(fl);
                }
            }
        }

        if (this.ticksTimer < 1) {
            this.setTicks(RoomItemFactory.getProcessTime(3));
        }
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        /*if (this.interactingEntities.containsKey(entity.getVirtualId())) { return; }
        this.interactingEntities.put(entity.getVirtualId(), entity);*/

        if (this.ticksTimer < 1) {
            this.setTicks(RoomItemFactory.getProcessTime(3));
        }
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {

    }

    @Override
    public void onTickComplete() {
        this.handleEntities();

        if (this.interactingItems.size() > 0) {
            this.handleItems();
        }
    }

    private void handleItems() {
        Position3D sqInfront = this.squareInfront();

        if (!this.getRoom().getMapping().isValidPosition(sqInfront)) {
            return;
        }

        List<RoomItemFloor> processedItems = new ArrayList<>();

        for (RoomItemFloor item : this.interactingItems) {
            processedItems.add(item);

            if (!this.getRoom().getMapping().isValidStep(new Position3D(item.getX(), item.getY()), sqInfront, true) || !this.getRoom().getEntities().isSquareAvailable(sqInfront.getX(), sqInfront.getY())) {
                break;
            }

            if (item.getX() != this.getX() && item.getY() != this.getY()) {
                continue;
            }

            float toHeight = 0f;
            boolean cancel = false;

            for (RoomItemFloor nextItem : this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                if (!nextItem.getDefinition().canStack) {
                    cancel = true;
                    break;
                }

                toHeight += nextItem.getDefinition().getHeight();
            }

            if (cancel) { break; }

            this.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(new Position3D(item.getX(), item.getY(), item.getHeight()), new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight), this.getId(), 0, item.getId()));

            item.setX(sqInfront.getX());
            item.setY(sqInfront.getY());
            item.setHeight(toHeight);

            for (AffectedTile affTile : AffectedTile.getAffectedBothTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(),
                    item.getX(), item.getY(), item.getRotation())) {
                this.getRoom().getMapping().updateTile(affTile.x, affTile.y);
            }
        }

        List<RoomItemFloor> floorItems = this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY());

        List<RoomItemFloor> allFloorItems = new ArrayList<>(floorItems);
        allFloorItems.add(this);

        for (RoomItemFloor stackItem : allFloorItems) {
            List<RoomItemFloor> itemsAboveAndBelow = new ArrayList<>();

            for (RoomItemFloor stackItem0 : allFloorItems) {
                if (stackItem.getId() != stackItem0.getId()) {
                    itemsAboveAndBelow.add(stackItem0);
                }
            }

            stackItem.onItemStacked(itemsAboveAndBelow);
        }

        for (RoomItemFloor item : processedItems) {
            this.interactingItems.remove(item);
        }
    }

    private void handleEntities() {
        Position3D sqInfront = this.squareInfront();

        if (!this.getRoom().getMapping().isValidPosition(sqInfront)) {
            return;
        }

        List<Integer> processedEntities = new ArrayList<>();

        List<GenericEntity> entites = this.getRoom().getEntities().getEntitiesAt(this.getX(), this.getY());

        for (GenericEntity entity : entites) {
            processedEntities.add(entity.getVirtualId());

            if (entity.getPosition().getX() != this.getX() && entity.getPosition().getY() != this.getY()) {
                continue;
            }

            if (!this.getRoom().getMapping().isValidStep(entity.getPosition(), sqInfront, true) || !this.getRoom().getEntities().isSquareAvailable(sqInfront.getX(), sqInfront.getY())) {
                break;
            }

            if (entity.isWalking()) {
                continue;
            }

            double toHeight = 0d;

            for (RoomItemFloor nextItem : this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                if (!nextItem.getDefinition().canSit) {
                    toHeight += nextItem.getDefinition().getHeight();
                }
            }

            entity.updateAndSetPosition(new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight));
            this.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(entity.getPosition(), new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight), this.getId(), entity.getVirtualId(), 0));
        }

        //for (Integer virtualId : processedEntities) {
        //    this.interactingEntities.remove(virtualId);
        //}
    }
}
