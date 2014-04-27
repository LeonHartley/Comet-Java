package com.cometproject.server.tasks;

import java.util.concurrent.*;

public class CometThreadManagement {
    private ExecutorService executionService;
    private ScheduledExecutorService scheduledExecutorService;

    public CometThreadManagement() {
        this.executionService = Executors.newCachedThreadPool();
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    public Future executeOnce(CometTask task) {
        return this.executionService.submit(task);
    }

    public ScheduledFuture executePeriodic(CometTask task, long initialDelay, long period, TimeUnit unit) {
        return this.scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public ScheduledFuture executeSchedule(CometTask task, long delay, TimeUnit unit) {
        return this.scheduledExecutorService.schedule(task, delay, unit);
    }
}