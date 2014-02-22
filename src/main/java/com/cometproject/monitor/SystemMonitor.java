package com.cometproject.monitor;

import java.util.concurrent.ScheduledFuture;
import com.cometproject.boot.Comet;
import com.cometproject.tasks.CometTask;
import com.cometproject.tasks.CometThreadManagement;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class SystemMonitor implements CometTask {
    private Logger log = Logger.getLogger(SystemMonitor.class.getName());
    private ScheduledFuture myFuture;

    private boolean active = false;
    private int cycleCount = 0;

    public SystemMonitor(CometThreadManagement mgr) {
        int interval = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.interval"));
        this.myFuture = mgr.executePeriodic(this, interval, interval, TimeUnit.SECONDS);
        this.active = true;
    }

    @Override
    public void run() {
        System.gc();
        cycleCount++;
    }

    public void stop() {
        this.active = false;
        this.myFuture.cancel(false);
    }

    public int getCycleCount() {
        return this.cycleCount;
    }
}
