package com.cometproject.server.tasks;

import java.util.UUID;
import java.util.concurrent.*;

public class CometThreadManagement {
    private final ExecutorService executionService;
    private final ScheduledExecutorService scheduledExecutorService;

    private final int initialpoolSize = (Runtime.getRuntime().availableProcessors() * 2);

    public CometThreadManagement() {
        this.executionService = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                UUID randomId = UUID.randomUUID();

                Thread workerThread = new Thread(r);
                workerThread.setName("Comet-Worker-Thread-" + randomId.toString());

                return workerThread;
            }
        });

        this.scheduledExecutorService = Executors.newScheduledThreadPool(this.initialpoolSize, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                UUID randomId = UUID.randomUUID();

                Thread scheduledThread = new Thread(r);
                scheduledThread.setName("Comet-Scheduled-Thread-" + randomId.toString());

                return scheduledThread;
            }
        });
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