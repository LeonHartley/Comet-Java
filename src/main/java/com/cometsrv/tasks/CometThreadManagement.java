package com.cometsrv.tasks;

import java.util.concurrent.*;

public class CometThreadManagement {
    private ExecutorService executionService;
    private ScheduledExecutorService scheduledExecutorService;

    public CometThreadManagement() {
        this.executionService = Executors.newCachedThreadPool();
        this.scheduledExecutorService = Executors.newScheduledThreadPool(0);
    }

    public Future executeOnce(CometTask task) {
        return this.executionService.submit(task);
    }

    public ScheduledFuture executePeriodic(CometTask task, long initialDelay, long period, TimeUnit unit) {
        return this.scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
    }
}
