package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOffFurni;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOnFurni;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

import java.util.List;


public class RollerFloorItem extends RoomItemFloor {
    public RollerFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onLoad() {
        this.setTicks(this.getTickCount());
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (entity.isWalking()) return;

        if (this.ticksTimer < 1) {
            this.setTicks(this.getTickCount());
        }
    }

    @Override
    public void onItemAddedToStack(RoomItemFloor floorItem) {
        if (this.ticksTimer < 1) {
            this.setTicks(this.getTickCount());
        }
    }

    @Override
    public void onTickComplete() {
        this.handleItems();
        this.handleEntities();
    }

    private void handleEntities() {
        RoomTile tile = this.getTile();

//        if(tile.getTopItem() != this.getId())
//            return;

        Position sqInfront = this.getPosition().squareInFront(this.getRotation());

        if (!this.getRoom().getMapping().isValidPosition(sqInfront)) {
            return;
        }

        boolean retry = false;

        List<RoomEntity> entities = this.getRoom().getEntities().getEntitiesAt(this.getPosition());

        for (RoomEntity entity : entities) {
            if (entity.getPosition().getX() != this.getPosition().getX() && entity.getPosition().getY() != this.getPosition().getY()) {
                continue;
            }

            if (entity.getPositionToSet() != null) {
                continue;
            }

            if (!this.getRoom().getMapping().isValidStep(entity.getId(), entity.getPosition(), sqInfront, true, false, false) || this.getRoom().getEntities().positionHasEntity(sqInfront)) {
                retry = true;
                break;
            }

            if (entity.isWalking()) {
                continue;
            }

            if (sqInfront.getX() == this.getRoom().getModel().getDoorX() && sqInfront.getY() == this.getRoom().getModel().getDoorY()) {
                entity.leaveRoom(false, false, true);
                continue;
            }

            WiredTriggerWalksOffFurni.executeTriggers(entity, this);

            for (RoomItemFloor nextItem : this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                WiredTriggerWalksOnFurni.executeTriggers(entity, nextItem);

                nextItem.onEntityStepOn(entity);
            }

            final double toHeight = this.getRoom().getMapping().getTile(sqInfront.getX(), sqInfront.getY()).getWalkHeight();

            final RoomTile oldTile = this.getRoom().getMapping().getTile(entity.getPosition().getX(), entity.getPosition().getY());
            final RoomTile newTile = this.getRoom().getMapping().getTile(sqInfront.getX(), sqInfront.getY());

            if (oldTile != null) {
                oldTile.getEntities().remove(entity);
            }

            if (newTile != null) {
                newTile.getEntities().add(entity);
            }

            this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(entity.getPosition(), new Position(sqInfront.getX(), sqInfront.getY(), toHeight), this.getVirtualId(), entity.getId(), 0));
            entity.setPosition(new Position(sqInfront.getX(), sqInfront.getY(), toHeight));
        }

        if (retry) {
            this.setTicks(this.getTickCount());
        }
    }

    private void handleItems() {
        List<RoomItemFloor> floorItems = this.getRoom().getItems().getItemsOnSquare(this.getPosition().getX(), this.getPosition().getY());

        if (floorItems.size() < 2) {
            return;
        }

        // quick check illegal use of rollers
        int rollerCount = 0;
        for (RoomItemFloor f : floorItems) {
            if (f instanceof RollerFloorItem) {
                rollerCount++;
            }
        }

        if (rollerCount > 1) {
            return;
        }

        Position sqInfront = this.getPosition().squareInFront(this.getRotation());

        boolean noItemsOnNext = false;

        for (RoomItemFloor floor : floorItems) {
            if (floor.getPosition().getX() != this.getPosition().getX() && floor.getPosition().getY() != this.getPosition().getY()) {
                continue;
            }

            if (floor instanceof RollerFloorItem || floor.getPosition().getZ() <= this.getPosition().getZ()) {
                continue;
            }

            if (!floor.getDefinition().canStack()) {
                if(floor.getTile().getTopItem() != floor.getId())
                    continue;
            }

            double height = floor.getPosition().getZ();

            List<RoomItemFloor> itemsSq = this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY());

            boolean hasRoller = false;

            for (RoomItemFloor iq : itemsSq) {
                if (iq instanceof RollerFloorItem) {
                    hasRoller = true;
                }
            }

            if (!hasRoller || noItemsOnNext) {
                height -= 0.5;
                noItemsOnNext = true;
            }

//            double heightDiff = 0;
//
//            if (itemsSq.size() > 1) {
//                RoomItemFloor item1 = itemsSq.get(0);
//                RoomItemFloor item2 = itemsSq.get(1);
//
//                heightDiff = item1.getPosition().getZ() - item2.getPosition().getZ();
//            }
//
//            if (heightDiff > -2) {
//                if (!this.getRoom().getMapping().isValidStep(new Position(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ()), sqInfront, true) || !this.getRoom().getEntities().positionHasEntity(sqInfront.getX(), sqInfront.getY())) {
//                    this.setTicks(3);
//                    break;
//                }
//            }

            if (!this.getRoom().getMapping().isValidStep(new Position(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ()), sqInfront, true) || this.getRoom().getEntities().positionHasEntity(sqInfront)) {
                this.setTicks(this.getTickCount());
                return;
            }

            this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(new Position(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ()), new Position(sqInfront.getX(), sqInfront.getY(), height), this.getVirtualId(), 0, floor.getVirtualId()));

            floor.getPosition().setX(sqInfront.getX());
            floor.getPosition().setY(sqInfront.getY());
            floor.getPosition().setZ(height);

            RoomItemDao.saveItemPosition(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ(), floor.getRotation(), floor.getId());
        }

        this.getRoom().getMapping().updateTile(this.getPosition().getX(), this.getPosition().getY());
        this.getRoom().getMapping().updateTile(sqInfront.getX(), sqInfront.getY());

        for (RoomItemFloor nextItem : this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
            for (RoomItemFloor floor : floorItems) {
                nextItem.onItemAddedToStack(floor);
            }
        }
    }

    private int getTickCount() {
//        return RoomItemFactory.getProcessTime(this.getRoom().hasAttribute("customRollerSpeed") ? (int) this.getRoom().getAttribute("customRollerSpeed") : 3);
        return RoomItemFactory.getProcessTime((this.getRoom().hasAttribute("customRollerSpeed") ? (int) this.getRoom().getAttribute("customRollerSpeed") : 4) / 2);
    }
}
