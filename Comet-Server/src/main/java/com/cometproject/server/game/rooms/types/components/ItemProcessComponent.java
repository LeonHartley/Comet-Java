package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.items.queue.RoomItemEventQueue;
import com.cometproject.server.game.rooms.objects.items.types.floor.RollerFloorItem;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.TimeSpan;
import org.apache.log4j.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class ItemProcessComponent implements CometTask {
    //    private final int INTERVAL = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.item_process.interval"));
    private static final int INTERVAL = 500;
    private static final int FLAG = 400;

    private final RoomInstance room;
    private final Logger log;

    private ScheduledFuture myFuture;
    private CometThreadManager mgr;

    private boolean active = false;

    private int wiredTimer = 0; // Increases every 0.5s and can be reset by wired.

    private final RoomItemEventQueue eventQueue = new RoomItemEventQueue();

    public ItemProcessComponent(CometThreadManager mgr, RoomInstance room) {
        this.mgr = mgr;
        this.room = room;

        log = Logger.getLogger("Item Process [" + room.getData().getName() + "]");
    }

    public RoomItemEventQueue getEventQueue() {
        return this.eventQueue;
    }

    public void start() {
        if (RoomInstance.useCycleForItems) {
            this.active = true;
            return;
        }

        if (this.myFuture != null && this.active) {
            stop();
        }

        this.active = true;
        this.myFuture = this.mgr.executePeriodic(this, 0, INTERVAL, TimeUnit.MILLISECONDS);

        log.debug("Processing started");
    }

    public void stop() {
        if (RoomInstance.useCycleForItems) {
            this.active = false;
            return;
        }

        if (this.myFuture != null) {
            this.active = false;
            this.myFuture.cancel(false);

            log.debug("Processing stopped");
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public void tick() {
        if (!this.active) {
            return;
        }

        long timeStart = System.currentTimeMillis();

        for (RoomItemFloor item : this.getRoom().getItems().getFloorItems()) {
            try {
                if (item != null && item.requiresTick() || item instanceof RollerFloorItem) {
                    item.tick();
                }
            } catch (Exception e) {
                this.handleException(item, e);
            }
        }

        for (RoomItemWall item : this.getRoom().getItems().getWallItems()) {
            try {
                if (item != null && item.requiresTick()) {
                    item.tick();
                }
            } catch (Exception e) {
                this.handleException(item, e);
            }
        }

        // Now lets process any queued events last
//        try {
//            this.eventQueue.tick();
//        } catch (NullPointerException | IndexOutOfBoundsException e) {
//            this.handleSupressedExceptions(e);
//        }

        TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

        if (span.toMilliseconds() > FLAG && Comet.isDebugging) {
            log.warn("ItemProcessComponent process took: " + span.toMilliseconds() + "ms to execute.");
        }
    }

    @Override
    public void run() {
        this.tick();
    }

    protected void handleException(RoomItem item, Exception e) {
        log.error("Error while processing item: " + item.getId() + " (" + item.getClass().getSimpleName() + ")", e);
    }

    public RoomInstance getRoom() {
        return this.room;
    }

}