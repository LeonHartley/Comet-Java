package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.components.ItemProcessComponent;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import org.apache.log4j.varia.Roller;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RollerFloorItem extends RoomItemFloor {
    //private List<RoomItemFloor> interactionFloorItems = new ArrayList<>();

    public RollerFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onItemStacked(List<RoomItemFloor> itemsInStack) {
        /*for (RoomItemFloor f : itemsInStack) {
            if (!interactionFloorItems.contains(f)) {
                if (!(f instanceof RollerFloorItem)) {
                    this.interactionFloorItems.add(f);
                }
            }
        }*/

        if (this.ticksTimer < 1) {
            this.setTicks(RoomItemFactory.getProcessTime(2));
        }
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        if (this.ticksTimer < 1) {
            this.setTicks(RoomItemFactory.getProcessTime(2));
        }
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {

    }

    @Override
    public void onTickComplete() {
        this.handleEntities();
        this.handleItems();
    }

    private void handleItems() {
        Position3D sqInfront = this.squareInfront();

        if (!this.getRoom().getMapping().isValidPosition(sqInfront)) {
            return;
        }

        List<AffectedTile> tilesToUpdate = new ArrayList<>();
        //List<RoomItemFloor> processedItems = new ArrayList<>();

        List<RoomItemFloor> items = this.getRoom().getItems().getItemsOnSquare(this.getX(), this.getY());

        for (RoomItemFloor item : items) {
            //processedItems.add(item);

            if (item instanceof RollerFloorItem || item.getHeight() < this.getHeight()) {
                continue;
            }

            if (!this.getRoom().getMapping().isValidStep(new Position3D(item.getX(), item.getY()), sqInfront, true) || !this.getRoom().getEntities().isSquareAvailable(sqInfront.getX(), sqInfront.getY())) {
                this.setTicks(RoomItemFactory.getProcessTime(2));
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

            RoomItemDao.saveItemPosition(item.getX(), item.getY(), item.getHeight(), item.getRotation(), item.getId());

            for (AffectedTile affTile : AffectedTile.getAffectedBothTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(),
                    item.getX(), item.getY(), item.getRotation())) {
                tilesToUpdate.add(affTile);
            }
        }

        for (AffectedTile affTile : tilesToUpdate) {
            this.getRoom().getMapping().updateTile(affTile.x, affTile.y);
        }

        List<RoomItemFloor> floorItems = this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY());

        if (floorItems.size() > 0) {
            RoomItemFloor next = floorItems.get(0);

            if (next instanceof RollerFloorItem) {
                /*List<RoomItemFloor> allFloorItems = new ArrayList<>(floorItems);
                //allFloorItems.add(next);

                for (RoomItemFloor stackItem : allFloorItems) {
                    List<RoomItemFloor> itemsAboveAndBelow = new ArrayList<>();

                    for (RoomItemFloor stackItem0 : allFloorItems) {
                        if (stackItem.getId() != stackItem0.getId()) {
                            itemsAboveAndBelow.add(stackItem0);
                        }
                    }

                    stackItem.onItemStacked(itemsAboveAndBelow);
                }*/

                next.onItemStacked(null);
            }
        }

        //for (RoomItemFloor f : processedItems) {
        //    this.interactionFloorItems.remove(f);
        //}

        this.getRoom().getMapping().updateTile(this.x, this.y);
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
                this.setTicks(3);
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
