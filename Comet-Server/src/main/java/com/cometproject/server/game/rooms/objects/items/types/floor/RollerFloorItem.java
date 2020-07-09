package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.events.types.RollerFloorItemEvent;
import com.cometproject.server.game.rooms.objects.items.types.AdvancedFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOffFurni;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOnFurni;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;
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
    private Set<Integer> previouslySkipped = Sets.newConcurrentHashSet();
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

        for (RollerFloorItem rollerItem : this.getRoom().getItems().getByClass(this.getClass())) {
            final RollerFloorItemEvent rollerFloorItemEvent = rollerItem.getNextEvent();
            if (rollerFloorItemEvent != null && rollerItem != this) {
                event.setTicks(rollerFloorItemEvent.getCurrentTicks());
                break;
            }
        }

        this.queueEvent(event);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (event.getCurrentTicks() >= this.getTickCount() - 1) {
            if (skippedEntities.contains(entity.getId())) {
                skippedEntities.remove(entity.getId());
            } else {
                skippedEntities.add(entity.getId());
            }
        }
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
    }

    @Override
    public void onItemAddedToStack(RoomItemFloor floorItem) {
        if (event.getCurrentTicks() >= this.getTickCount() - 1) {
            skippedItems.add(floorItem.getVirtualId());
        }
    }

    @Override
    public void onEventComplete(RollerFloorItemEvent event) {
        if (this.cycleCancelled) {
            this.cycleCancelled = false;
        }

        long startTime = System.currentTimeMillis();

        Comet.getServer().getLogger().debug(this.getId() + " onEventComplete called");

        // don't do it if skipped?

        if (this.handleItems()) {
            this.handleEntities();
        }

        this.movedEntities.clear();
        this.skippedEntities.clear();
        this.skippedItems.clear();

        event.setTotalTicks(this.getTickCount());
        this.queueEvent(event);
        Comet.getServer().getLogger().debug("onEventComplete took " + (System.currentTimeMillis() - startTime));
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

            if (entity.isWalking()) {
                entity.setProcessingPath(null);
                entity.setWalkingPath(null);
            }

            if (!this.getRoom().getMapping().isValidStep(entity.getId(), entity.getPosition(), sqInfront, true, false, false, true, false) || this.getRoom().getEntities().positionHasEntity(sqInfront)) {
                retry = true;
                break;
            }

            if (sqInfront.getX() == this.getRoom().getModel().getDoorX() && sqInfront.getY() == this.getRoom().getModel().getDoorY()) {
                entity.leaveRoom(false, false, true);
                continue;
            }

            WiredTriggerWalksOffFurni.executeTriggers(entity, this);

            final double toHeight = this.getRoom().getMapping().getTile(sqInfront.getX(), sqInfront.getY()).getWalkHeight();

            this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(entity.getPosition().copy(), new Position(sqInfront.getX(), sqInfront.getY(), toHeight), this.getVirtualId(), entity.getId(), 0));

            entity.updateAndSetPosition(new Position(sqInfront.getX(), sqInfront.getY(), toHeight));
            entity.markNeedsUpdate(true);

            this.onEntityStepOff(entity);
            movedEntities.add(entity);
        }

        if (retry) {
            this.cycleCancelled = true;
        }
    }

    private boolean handleItems() {
        List<RoomItemFloor> floorItems = this.getRoom().getItems().getItemsOnSquare(this.getPosition().getX(), this.getPosition().getY());

        if (floorItems.size() < 2) {
            return true;
        }

        // quick check illegal use of rollers
        int rollerCount = 0;
        for (RoomItemFloor f : floorItems) {
            if (f instanceof RollerFloorItem) {
                rollerCount++;
            }
        }

        if (rollerCount > 1) {
            return true;
        }

        final Position sqInfront = this.getPosition().squareInFront(this.getRotation());
        List<RoomItemFloor> itemsNextSquare = this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY());

        boolean noItemsOnNext = false;

        Position position = null;
        final Map<Integer, Double> slidingItems = Maps.newHashMap();

        for (RoomItemFloor floor : floorItems) {
            if (floor.getId() == this.getId() || floor.getPosition().getX() != this.getPosition().getX() && floor.getPosition().getY() != this.getPosition().getY()) {
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

            RoomItemFloor nextRoller = null;

            for (RoomItemFloor iq : itemsNextSquare) {
                if (iq instanceof RollerFloorItem) {
                    nextRoller = iq;

                    if (iq.getPosition().getZ() != this.getPosition().getZ()) {
                        height -= this.getPosition().getZ();
                        height += iq.getPosition().getZ();
                    }

                    break;
                }
            }

            if (nextRoller == null || noItemsOnNext) {
                height -= 0.5;
                noItemsOnNext = true;
            }

            if (nextRoller != null) {// && rollerIsFacing) {
                boolean itemsAboveRoller = false;

                for (RoomItemFloor iq : itemsNextSquare) {
                    if (iq != nextRoller && iq.getPosition().getZ() >= nextRoller.getPosition().getZ()) {
                        itemsAboveRoller = true;
                        break;
                    }
                }

                if (itemsAboveRoller) {
                    return false;
                }
            }

            if (!this.getRoom().getMapping().isValidStep(null, new Position(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ()), sqInfront, true, false, false, true, true) || this.getRoom().getEntities().positionHasEntity(sqInfront, this.movedEntities)) {
                return false;
            }

            slidingItems.put(floor.getVirtualId(), height);


            for (RoomEntity roomEntity : this.movedEntities) {
                WiredTriggerWalksOnFurni.executeTriggers(roomEntity, floor);
                floor.onEntityStepOn(roomEntity);
            }

            floor.setPosition(new Position(sqInfront.getX(), sqInfront.getY(), height));

            Comet.getServer().getLogger().debug(this.getId() + " moved an item");
            this.getRoom().getItemProcess().saveItem(floor);
        }

        if (slidingItems.size() != 0) {
            this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(position, sqInfront.copy(), this.getVirtualId(), 0, slidingItems));
        }

        this.getRoom().getMapping().updateTile(this.getPosition().getX(), this.getPosition().getY());
        this.getRoom().getMapping().updateTile(sqInfront.getX(), sqInfront.getY());

        for (RoomItemFloor nextItem : this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
            for (RoomItemFloor floor : floorItems) {
                nextItem.onItemAddedToStack(floor);
            }
        }

        return true;
    }

    private int getTickCount() {
//        return RoomItemFactory.getProcessTime(this.getRoom().hasAttribute("customRollerSpeed") ? (int) this.getRoom().getAttribute("customRollerSpeed") : 3);
        return RoomItemFactory.getProcessTime((this.getRoom().hasAttribute("customRollerSpeed") ? (int) this.getRoom().getAttribute("customRollerSpeed") : 4) / 2f);
    }
}
