package com.cometsrv.tasks;

import com.cometsrv.boot.Comet;

import java.util.concurrent.*;

public class CometThreadManagement {
    private ExecutorService executionService;
    private ScheduledExecutorService scheduledExecutorService;

    private Integer scheduledPoolMinSize = Integer.parseInt(Comet.getServer().getConfig().get("comet.threading.scheduler.poolMinSize"));

    public CometThreadManagement() {
        this.executionService = Executors.newCachedThreadPool();
        this.scheduledExecutorService = Executors.newScheduledThreadPool(this.scheduledPoolMinSize);
    }

    public Future executeOnce(CometTask task) {
        return this.executionService.submit(task);
    }

    public ScheduledFuture executePeriodic(CometTask task, long initialDelay, long period, TimeUnit unit) {
        return this.scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
    }
}
