package com.cometproject.server.tasks;

import org.apache.log4j.Logger;

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

                final Logger log = Logger.getLogger("Comet-Worker-Thread-" + randomId);

                Thread workerThread = new Thread(r);
                workerThread.setName("Comet-Worker-Thread-" + randomId.toString());

                workerThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        log.error("Exception in Comet Worker Thread", e);
                    }
                });

                return workerThread;
            }
        });

        this.scheduledExecutorService = Executors.newScheduledThreadPool(this.initialpoolSize, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                UUID randomId = UUID.randomUUID();

                Thread scheduledThread = new Thread(r);
                scheduledThread.setName("Comet-Scheduled-Thread-" + randomId.toString());

                final Logger log = Logger.getLogger("Comet-Scheduled-Thread-" + randomId);

                scheduledThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        log.error("Exception in Comet Worker Thread", e);
                    }
                });

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