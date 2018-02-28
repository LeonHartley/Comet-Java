package com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.items.types.LowPriorityItemProcessor;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.RollableFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.state.FloorItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.utilities.RandomUtil;

import java.util.HashSet;
import java.util.Set;


public class BanzaiTeleporterFloorItem extends RoomItemFloor {
    private int stage = 0;

    private Position teleportPosition;
    private RoomEntity entity;
    private RoomItemFloor floorItem;

    public BanzaiTeleporterFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
        this.getItemData().setData(0);
    }

//    @Override
//    public void onEventComplete(BanzaiTeleportEvent event) {
//
//    }

    @Override
    public void onItemAddedToStack(RoomItemFloor floorItem) {
        if (this.floorItem != null) return;

        if (!(floorItem instanceof RollableFloorItem)) {
            return;
        }

        if (floorItem.hasAttribute("warp")) {
            this.stage = 2;
            this.setTicks(RoomItemFactory.getProcessTime(0.25));

            floorItem.removeAttribute("warp");
            return;
        }

        final Position teleportPosition = this.findPosition();

        if (teleportPosition == null) {
            return;
        }

        this.teleportPosition = teleportPosition;

        this.floorItem = floorItem;
        this.floorItem.setAttribute("warp", true);

        this.setTicks(LowPriorityItemProcessor.getProcessTime(0.25));
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (this.entity != null) return; // wait yer turn

        if (entity.hasAttribute("warp")) {
            this.stage = 2;
            this.setTicks(LowPriorityItemProcessor.getProcessTime(0.25));

            entity.removeAttribute("warp");
            return;
        }


        final Position teleportPosition = this.findPosition();

        if (teleportPosition == null) {
            return;
        }

        this.teleportPosition = teleportPosition;

        this.entity = entity;
        this.entity.setAttribute("warp", true);

        this.getItemData().setData("1");
        this.sendUpdate();

        this.stage = 1;

        entity.cancelWalk();
        this.setTicks(LowPriorityItemProcessor.getProcessTime(0.25));
    }

    private Position findPosition() {
        Set<BanzaiTeleporterFloorItem> teleporters = new HashSet<>();

        for (RoomItemFloor tele : this.getRoom().getItems().getFloorItems().values()) {
            if (tele instanceof BanzaiTeleporterFloorItem) {
                if (tele.getId() != this.getId())
                    teleporters.add((BanzaiTeleporterFloorItem) tele);
            }
        }

        if (teleporters.size() < 1)
            return null;

        BanzaiTeleporterFloorItem randomTeleporter = (BanzaiTeleporterFloorItem) teleporters.toArray()[RandomUtil.getRandomInt(0, teleporters.size() - 1)];
        teleporters.clear();

        return new Position(randomTeleporter.getPosition().getX(), randomTeleporter.getPosition().getY(), randomTeleporter.getTile().getWalkHeight());
    }

    @Override
    public void onTickComplete() {
        if (this.stage == 1) {
            if (this.floorItem != null) {
                this.floorItem.getPosition().setX(this.teleportPosition.getX());
                this.floorItem.getPosition().setY(this.teleportPosition.getY());

                for (RoomItemFloor floorItem : this.getRoom().getItems().getItemsOnSquare(this.teleportPosition.getX(), this.teleportPosition.getY())) {
                    floorItem.onItemAddedToStack(this);
                }

                this.floorItem.getPosition().setZ(this.teleportPosition.getZ());
                this.getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(floorItem));
            }

            if (this.entity != null) {
//                final RoomTile tile = this.getRoom().getMapping().getTile(this.teleportPosition);

                this.entity.warp(this.teleportPosition.copy(), false);
//                tile.getTopItemInstance().onEntityStepOn(this.entity);

                this.entity = null;
            }

            this.teleportPosition = null;

            this.setTicks(LowPriorityItemProcessor.getProcessTime(0.5));
            this.stage = 0;
            return;
        } else if (this.stage == 2) {
            this.getItemData().setData("1");
            this.sendUpdate();

            this.setTicks(LowPriorityItemProcessor.getProcessTime(0.5));
            this.stage = 0;
            return;
        }

        this.getItemData().setData("0");
        this.sendUpdate();
    }

    public class BanzaiTeleportEvent extends FloorItemEvent {

        protected static final int OUTGOING = 2;
        protected static final int INCOMING = 1;

        private final int event;

        protected BanzaiTeleportEvent(int event) {
            super(1);

            this.event = event;
        }
    }
}