package com.cometproject.server.game.rooms.types.components;

import com.cometproject.api.game.rooms.objects.IRoomItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.objects.entities.WiredTriggerExecutor;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.items.types.floor.RollerFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerPeriodically;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.TimeSpan;
import com.cometproject.storage.api.StorageContext;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class ItemProcessComponent implements CometTask {
    private static final int INTERVAL = 500;
    private static final int FLAG = 400;
    private final Room room;
    private final Logger log;

    private ScheduledFuture future;
    private ScheduledFuture saveFuture;

    private final Queue<RoomItem> saveQueue = Queues.newConcurrentLinkedQueue();

    private boolean active = false;

    public ItemProcessComponent(Room room) {
        this.room = room;

        log = Logger.getLogger("Item Process [" + room.getData().getName() + "]");
    }

    public void start() {
        if (Room.useCycleForItems) {
            this.active = true;
            return;
        }

        if (this.future != null && this.active) {
            stop();
        }

        this.active = true;

        this.future = CometThreadManager.getInstance().executePeriodic(this, 0, INTERVAL, TimeUnit.MILLISECONDS);
        this.saveFuture = CometThreadManager.getInstance().executePeriodic(this::saveQueueTick, 1000, 1000, TimeUnit.MILLISECONDS);

        log.debug("Processing started");
    }

    private void saveQueueTick() {
        RoomItem roomItem;

        final Set<IRoomItemData> items = Sets.newHashSet();

        while((roomItem = this.saveQueue.poll()) != null) {
            items.add(roomItem.getItemData());
        }

        StorageContext.getCurrentContext().getRoomItemRepository().saveItemBatch(items);
    }

    public void stop() {
        if (Room.useCycleForItems) {
            this.active = false;
            return;
        }

        if (this.future != null) {
            this.active = false;

            this.future.cancel(false);
            this.saveFuture.cancel(false);

            this.saveQueueTick();

            log.debug("Processing stopped");
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public void processTick() {
        if (!this.active) {
            return;
        }

        long timeStart = System.currentTimeMillis();

        if (this.getRoom().getEntities().realPlayerCount() == 0) return;

        final Set<String> positionsWithPeriodicTrigger = new HashSet<>();

        for (RoomItemFloor item : this.getRoom().getItems().getFloorItems().values()) {
            try {
                if (item != null && item.requiresTick() || item instanceof RollerFloorItem) {
                    if (item instanceof WiredTriggerPeriodically) {
                        final String posStr = item.getPosition().getX() + "_" + item.getPosition().getY();

                        if (positionsWithPeriodicTrigger.contains(posStr)) {
                            continue;
                        } else {
                            positionsWithPeriodicTrigger.add(posStr);
                        }
                    }

                    if (item.isStateSwitched()) {
                        item.restoreState();
                    }

                    item.tick();
                }
            } catch (Exception e) {
                this.handleException(item, e);
            }
        }

        for (RoomItemWall item : this.getRoom().getItems().getWallItems().values()) {
            try {
                if (item != null && item.requiresTick()) {
                    item.tick();
                }
            } catch (Exception e) {
                this.handleException(item, e);
            }
        }

        TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

        if (span.toMilliseconds() > FLAG && Comet.isDebugging) {
            log.warn("ItemProcessComponent process took: " + span.toMilliseconds() + "ms to execute.");
        }
    }

    @Override
    public void run() {
        this.processTick();
    }

    public void queueAction(WiredTriggerExecutor action) {
        // TODO: monitor this
        CometThreadManager.getInstance().executeOnce(action);
    }

    public void saveItem(RoomItem roomItem) {
        this.saveQueue.remove(roomItem);

        this.saveQueue.add(roomItem);
    }

    protected void handleException(RoomItem item, Exception e) {
        log.error("Error while processing item: " + item.getId() + " (" + item.getClass().getSimpleName() + ")", e);
    }

    public Room getRoom() {
        return this.room;
    }

}
