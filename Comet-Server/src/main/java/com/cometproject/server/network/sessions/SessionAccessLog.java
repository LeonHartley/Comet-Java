package com.cometproject.server.network.sessions;

import java.util.concurrent.atomic.AtomicInteger;

public class SessionAccessLog {
    private final AtomicInteger counter;
    private long lastConnection;

    public SessionAccessLog() {
        this.counter = new AtomicInteger(1);
        this.lastConnection = System.currentTimeMillis();
    }

    public void incrementCounter() {
        this.counter.incrementAndGet();
        this.lastConnection = System.currentTimeMillis();
    }

    private void resetCounter() {
        this.counter.set(0);
        this.lastConnection = 0;
    }

    public boolean isSuspicious() {
        final boolean suspiciousTime = (System.currentTimeMillis() - this.lastConnection) < 10000;

        if (!suspiciousTime) {
            this.resetCounter();
        } else {
            return this.counter.get() >= 30;
        }

        return false;
    }
}
