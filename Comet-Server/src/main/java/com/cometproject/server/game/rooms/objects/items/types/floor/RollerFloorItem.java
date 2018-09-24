package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.events.types.RollerFloorItemEvent;
import com.cometproject.server.game.rooms.objects.items.types.AdvancedFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOffFurni;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;
import com.cometproject.storage.api.StorageContext;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class RollerFloorItem extends AdvancedFloorItem<RollerFloorItemEvent> {
    private final RollerFloorItemEvent event;
    private boolean hasRollScheduled = false;
    private long lastTick = 0;
    private boolean cycleCancelled = false;
    private Set<Integer> skippedEntities = Sets.newConcurrentHashSet();
    private Set<Integer> skippedItems = Sets.newConcurrentHashSet();
    private Set<RoomEntity> movedEntities = new ConcurrentHashSet<>();

    public RollerFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);

        this.event = new RollerFloorItemEvent(this.getTickCount());
        this.queueEvent(event);
    }

    @Override
    public void onLoad() {
        event.setTotalTicks(this.getTickCount());
        this.queueEvent(event);
    }

    @Override
    public void onPlaced() {
        event.setTotalTicks(this.getTickCount());
        this.queueEvent(event);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        skippedEntities.add(entity.getId());

    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
    }

    @Override
    public void onItemAddedToStack(RoomItemFloor floorItem) {
        skippedItems.add(floorItem.getVirtualId());

    }

    @Override
    public void onEventComplete(RollerFloorItemEvent event) {
        if (this.cycleCancelled) {
            this.cycleCancelled = false;
        }

//        if (!cycleCancelled) {
        this.handleItems();
//        }

        this.handleEntities();

        this.movedEntities.clear();
        this.skippedEntities.clear();
        this.skippedItems.clear();

        event.setTotalTicks(this.getTickCount());
        this.queueEvent(event);
    }

    private void handleEntities() {
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

            if (this.skippedEntities.contains(entity.getId())) {
                continue;
            }

            if (entity.getPositionToSet() != null) {
                continue;
            }

            if (!this.getRoom().getMapping().isValidStep(entity.getId(), entity.getPosition(), sqInfront, true, false, false, true, false) || this.getRoom().getEntities().positionHasEntity(sqInfront)) {
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

            final double toHeight = this.getRoom().getMapping().getTile(sqInfront.getX(), sqInfront.getY()).getWalkHeight();

            this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(entity.getPosition().copy(), new Position(sqInfront.getX(), sqInfront.getY(), toHeight), this.getVirtualId(), entity.getId(), 0));

            entity.updateAndSetPosition(new Position(sqInfront.getX(), sqInfront.getY(), toHeight));
            entity.markNeedsUpdate(false);

            this.onEntityStepOff(entity);
            movedEntities.add(entity);
        }

        if (retry) {
            this.cycleCancelled = true;
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

        final Position sqInfront = this.getPosition().squareInFront(this.getRotation());
        List<RoomItemFloor> itemsSq = this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY());

        boolean noItemsOnNext = false;

        Position position = null;
        final Map<Integer, Double> slidingItems = Maps.newHashMap();

        for (RoomItemFloor floor : floorItems) {
            if (floor.getPosition().getX() != this.getPosition().getX() && floor.getPosition().getY() != this.getPosition().getY()) {
                continue;
            }

            if (this.skippedItems.contains(floor.getVirtualId())) {
                continue;
            }

            if (floor instanceof RollerFloorItem || floor.getPosition().getZ() <= this.getPosition().getZ()) {
                continue;
            }

            if (!floor.getDefinition().canStack() && !(floor instanceof RollableFloorItem)) {
                if (floor.getTile().getTopItem() != floor.getId())
                    continue;
            }

            if (position == null) {
                position = floor.getPosition().copy();
            }

            double height = floor.getPosition().getZ();

            boolean hasRoller = false;

            for (RoomItemFloor iq : itemsSq) {
                if (iq instanceof RollerFloorItem) {
                    hasRoller = true;

                    if (iq.getPosition().getZ() != this.getPosition().getZ()) {
                        height -= this.getPosition().getZ();
                        height += iq.getPosition().getZ();
                    }
                }
            }

            if (!hasRoller || noItemsOnNext) {
                height -= 0.5;
                noItemsOnNext = true;
            }

            if (hasRoller) {// && rollerIsFacing) {
                if (itemsSq.size() > 1) {
                    return;
                }
            }

            if (!this.getRoom().getMapping().isValidStep(null, new Position(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ()), sqInfront, true, false, false, true, true) || this.getRoom().getEntities().positionHasEntity(sqInfront, this.movedEntities)) {
                return;
            }

            slidingItems.put(floor.getVirtualId(), height);

            floor.getPosition().setX(sqInfront.getX());
            floor.getPosition().setY(sqInfront.getY());
            floor.getPosition().setZ(height);

            for (RoomEntity roomEntity : this.movedEntities) {
                floor.onEntityStepOn(roomEntity);
            }

            this.getRoom().getItemProcess().saveItem(floor);
        }

        if (slidingItems.size() != 0)
            this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(position, sqInfront.copy(), this.getVirtualId(), 0, slidingItems));

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