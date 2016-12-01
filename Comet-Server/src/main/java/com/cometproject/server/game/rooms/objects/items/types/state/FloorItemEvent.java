package com.cometproject.server.game.rooms.objects.items.types.state;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class FloorItemEvent {
    private final AtomicInteger ticks;
    private int totalTicks;

    protected FloorItemEvent(int totalTicks) {
        this.ticks = new AtomicInteger(0);
        this.totalTicks = totalTicks;
    }

    public void setTotalTicks(final int ticks) {
        this.totalTicks = ticks;
    }

    public void incrementTicks() {
        this.ticks.incrementAndGet();
    }

    public boolean isFinished() {
        return this.ticks.get() >= this.totalTicks;
    }
}
