package com.cometproject.server.tasks;

import org.apache.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.*;

public class CometThreadManagement {
    private final ScheduledExecutorService scheduledExecutorService;

    public CometThreadManagement() {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(2, r -> {
            UUID randomId = UUID.randomUUID();

            Thread scheduledThread = new Thread(r);
            scheduledThread.setName("Comet-Scheduled-Thread-" + randomId.toString());

            final Logger log = Logger.getLogger("Comet-Scheduled-Thread-" + randomId);
            scheduledThread.setUncaughtExceptionHandler((t, e) -> log.error("Exception in Comet Worker Thread", e));

            return scheduledThread;
        });
    }

    public Future executeOnce(CometTask task) {
        return this.scheduledExecutorService.submit(task);
    }

    public ScheduledFuture executePeriodic(CometTask task, long initialDelay, long period, TimeUnit unit) {
        return this.scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public ScheduledFuture executeSchedule(CometTask task, long delay, TimeUnit unit) {
        return this.scheduledExecutorService.schedule(task, delay, unit);
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }
}