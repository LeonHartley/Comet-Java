package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.items.queue.RoomItemEventQueue;
import com.cometproject.server.game.rooms.objects.items.types.floor.RollableFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.RollerFloorItem;
import com.cometproject.server.game.rooms.types.Room;
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

    private final Room room;
    private final Logger log;

    private ScheduledFuture myFuture;

    private boolean active = false;

    // TODO: Finish the item event queue.
    private RoomItemEventQueue eventQueue;// = new RoomItemEventQueue();

    public ItemProcessComponent(Room room) {
        this.room = room;

        log = Logger.getLogger("Item Process [" + room.getData().getName() + "]");
    }

    public RoomItemEventQueue getEventQueue() {
        return this.eventQueue;
    }

    public void start() {
        if (Room.useCycleForItems) {
            this.active = true;
            return;
        }

        if (this.myFuture != null && this.active) {
            stop();
        }

        this.active = true;
        this.myFuture = CometThreadManager.getInstance().executePeriodic(this, 0, INTERVAL, TimeUnit.MILLISECONDS);

        log.debug("Processing started");
    }

    public void stop() {
        if (Room.useCycleForItems) {
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

        if (this.getRoom().getEntities().realPlayerCount() == 0) return;

        for (RoomItemFloor item : this.getRoom().getItems().getFloorItems().values()) {
            try {
                if (item != null && !(item instanceof RollableFloorItem) && item.requiresTick() || item instanceof RollerFloorItem) {
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

    public Room getRoom() {
        return this.room;
    }

}