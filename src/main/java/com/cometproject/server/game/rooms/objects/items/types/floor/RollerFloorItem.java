package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

import java.util.List;

public class RollerFloorItem extends RoomItemFloor {
    public RollerFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onLoad() {
        this.setTicks(this.getTickCount());
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
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
//        final int rollerSpeed = this.getRoom().hasAttribute("customRollerSpeed") ? (int) this.getRoom().getAttribute("customRollerSpeed") : 3;
//
//        if(this.ticker > rollerSpeed) // reset the ticker if the roller speed has been manaully edited
//            this.ticker = 0;
//
//        if(ticker != rollerSpeed) {
//            ticker++;
//            return;
//        }

        this.handleEntities();
        this.handleItems();
    }

    private void handleEntities() {
        Position sqInfront = this.getPosition().squareInFront(this.getRotation());

        if (!this.getRoom().getMapping().isValidPosition(sqInfront)) {
            return;
        }

        List<GenericEntity> entities = this.getRoom().getEntities().getEntitiesAt(this.getPosition().getX(), this.getPosition().getY());

        for (GenericEntity entity : entities) {
            if (entity.getPosition().getX() != this.getPosition().getX() && entity.getPosition().getY() != this.getPosition().getY()) {
                continue;
            }

            if (!this.getRoom().getMapping().isValidStep(entity.getPosition(), sqInfront, true) || !this.getRoom().getEntities().isSquareAvailable(sqInfront.getX(), sqInfront.getY())) {
                break;
            }

            if (entity.isWalking()) {
                continue;
            }

            for (RoomItemFloor nextItem : this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                nextItem.onEntityStepOn(entity);
            }

            final double toHeight = this.getRoom().getMapping().getTile(sqInfront.getX(), sqInfront.getY()).getWalkHeight();

            this.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(entity.getPosition(), new Position(sqInfront.getX(), sqInfront.getY(), toHeight), this.getId(), entity.getId(), 0));
            entity.setPosition(new Position(sqInfront.getX(), sqInfront.getY(), toHeight));
//            entity.cancelNextUpdate();
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

            if (floor instanceof RollerFloorItem || floor.getPosition().getZ() < 0.5) {
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
//                if (!this.getRoom().getMapping().isValidStep(new Position(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ()), sqInfront, true) || !this.getRoom().getEntities().isSquareAvailable(sqInfront.getX(), sqInfront.getY())) {
//                    this.setTicks(3);
//                    break;
//                }
//            }

            if (!this.getRoom().getMapping().isValidStep(new Position(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ()), sqInfront, true) || !this.getRoom().getEntities().isSquareAvailable(sqInfront.getX(), sqInfront.getY())) {
                    this.setTicks(this.getTickCount());
                    return;
            }

            this.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(new Position(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ()), new Position(sqInfront.getX(), sqInfront.getY(), height), this.getId(), 0, floor.getId()));

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
        return this.getRoom().hasAttribute("customRollerSpeed") ? (int) this.getRoom().getAttribute("customRollerSpeed") : 3;
    }
}
