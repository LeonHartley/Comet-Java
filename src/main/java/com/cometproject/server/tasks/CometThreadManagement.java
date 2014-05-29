package com.cometproject.server.tasks;

import com.cometproject.server.boot.Comet;
import javolution.util.FastTable;
import org.apache.log4j.Logger;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.*;

public class CometThreadManagement {
    private final ScheduledExecutorService scheduledExecutorService;

    private final int initialpoolSize;

    private final FastTable<WeakReference<Thread>> threadMonitoring = new FastTable<>();
    private Future<?> monitorThread;

    public CometThreadManagement() {
        int poolSize = Integer.parseInt(Comet.getServer().getConfig().get("comet.threading.pool.size"));

        if (poolSize < 1) {
            initialpoolSize = (Runtime.getRuntime().availableProcessors() * 2);
        } else {
            initialpoolSize = poolSize;
        }

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
                        e.printStackTrace();
                    }
                });

                threadMonitoring.add(new WeakReference<Thread>(scheduledThread));
                return scheduledThread;
            }
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

    public void startMonitoring() {
        if (this.monitorThread != null) { return; }
    }

    private final class ThreadMonitorCycle implements CometTask {
        private final FastTable<WeakReference<Thread>> threads;

        public ThreadMonitorCycle(FastTable<WeakReference<Thread>> threads) {
            this.threads = threads;
        }

        @Override
        public void run() {
            for (int i = 0; i < threads.size(); i++) {
                WeakReference<Thread> threadWeakReference = threads.get(i);

                if (threadWeakReference != null) {
                    if (threadWeakReference.isEnqueued() || threadWeakReference.get() == null) {
                        threads.remove(i);
                    } else {

                    }
                }
            }
        }
    }
}