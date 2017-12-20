package com.cometproject.server.game.rooms.objects.items.types.state;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class FloorItemEvent {
    private final AtomicInteger ticks;
    private int totalTicks;

    protected FloorItemEvent(int totalTicks) {
        this.ticks = new AtomicInteger(0);
        this.totalTicks = totalTicks;
    }

    /**
     * You can override this to FORCE a callback! (even if the onEventComplete method is overriden)
     */
    public void onCompletion(final RoomItemFloor floorItem) {

    }

    public void setTotalTicks(final int ticks) {
        this.ticks.set(0);
        this.totalTicks = ticks;
    }

    public void incrementTicks() {
        this.ticks.incrementAndGet();
    }

    public boolean isFinished() {
        return this.ticks.get() >= this.totalTicks;
    }

    public boolean isInteractiveEvent() {
        return true;
    }

    public int getCurrentTicks() {
        return this.ticks.get();
    }
}
