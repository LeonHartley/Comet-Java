package com.cometproject.server.tasks;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.concurrent.*;


public class CometThreadManager implements Initializable {
    private static CometThreadManager cometThreadManagerInstance;

    public static int POOL_SIZE = 0;
    private ScheduledExecutorService scheduledExecutorService;

    public CometThreadManager() {

    }

    public static CometThreadManager getInstance() {
        if (cometThreadManagerInstance == null)
            cometThreadManagerInstance = new CometThreadManager();

        return cometThreadManagerInstance;
    }

    @Override
    public void initialize() {
        int poolSize = Integer.parseInt((String) Comet.getServer().getConfig().getOrDefault("comet.system.threads", "8"));

        this.scheduledExecutorService = Executors.newScheduledThreadPool(poolSize, r -> {
            POOL_SIZE++;

            Thread scheduledThread = new Thread(r);
            scheduledThread.setName("Comet-Scheduler-Thread-" + POOL_SIZE);

            final Logger log = Logger.getLogger("Comet-Scheduler-Thread-" + POOL_SIZE);
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