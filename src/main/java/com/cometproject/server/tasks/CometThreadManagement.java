package com.cometproject.server.tasks;

import java.util.concurrent.*;

public class CometThreadManagement {
    private ExecutorService executionService;
    private ScheduledExecutorService scheduledExecutorService;

    public CometThreadManagement() {
        this.executionService = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
        this.scheduledExecutorService = Executors.newScheduledThreadPool(0, Executors.defaultThreadFactory());
    }

    public Future executeOnce(CometTask task) {
        return this.executionService.submit(task);
    }

    public ScheduledFuture executePeriodic(CometTask task, long initialDelay, long period, TimeUnit unit) {
        return this.scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
    }
}