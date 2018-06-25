package com.cometproject.server.tasks;

import com.cometproject.api.config.Configuration;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.game.rooms.types.components.ItemProcessComponent;
import com.cometproject.server.game.rooms.types.components.ProcessComponent;
import org.apache.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class CometThreadManager implements Initialisable {
    public static int POOL_SIZE = 0;
    private static CometThreadManager cometThreadManagerInstance;
    private ScheduledExecutorService coreExecutor;
    private ScheduledExecutorService roomProcessingExecutor;

    public CometThreadManager() {

    }

    public static CometThreadManager getInstance() {
        if (cometThreadManagerInstance == null)
            cometThreadManagerInstance = new CometThreadManager();

        return cometThreadManagerInstance;
    }

    @Override
    public void initialize() {
        int poolSize = Integer.parseInt((String) Configuration.currentConfig().getOrDefault("comet.system.threads", "8"));

        this.coreExecutor = Executors.newScheduledThreadPool(poolSize, r -> {
            POOL_SIZE++;

            Thread scheduledThread = new Thread(r);
            scheduledThread.setName("Comet-Scheduler-Thread-" + POOL_SIZE);

            final Logger log = Logger.getLogger("Comet-Scheduler-Thread-" + POOL_SIZE);
            scheduledThread.setUncaughtExceptionHandler((t, e) -> log.error("Exception in worker thread", e));

            return scheduledThread;
        });

        final int roomProcessingPool = 8;
        final AtomicInteger counter = new AtomicInteger();

        this.roomProcessingExecutor = Executors.newScheduledThreadPool(roomProcessingPool, r -> {
            Thread scheduledThread = new Thread(r);
            scheduledThread.setName("Room-Processor-" + counter.incrementAndGet());

            final Logger log = Logger.getLogger(scheduledThread.getName());
            scheduledThread.setUncaughtExceptionHandler((t, e) -> log.error("Exception in room worker thread", e));

            return scheduledThread;
        });
    }

    public Future executeOnce(CometTask task) {
        return this.coreExecutor.submit(task);
    }

    public ScheduledFuture executePeriodic(CometTask task, long initialDelay, long period, TimeUnit unit) {
        if (task instanceof ProcessComponent || task instanceof ItemProcessComponent) {
            // Handle room processing in a different pool, this should help against
            return this.roomProcessingExecutor.scheduleAtFixedRate(task, initialDelay, period, unit);
        }

        return this.coreExecutor.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public ScheduledFuture executeSchedule(CometTask task, long delay, TimeUnit unit) {
        if (task instanceof ProcessComponent) {
            return this.roomProcessingExecutor.schedule(task, delay, unit);
        }

        return this.coreExecutor.schedule(task, delay, unit);
    }

    public ScheduledExecutorService getCoreExecutor() {
        return coreExecutor;
    }
}