package com.cometsrv.monitor;

import java.util.concurrent.ScheduledFuture;
import com.cometsrv.boot.Comet;
import com.cometsrv.tasks.CometTask;
import com.cometsrv.tasks.CometThreadManagement;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class SystemMonitor implements CometTask {
    private Logger log = Logger.getLogger(SystemMonitor.class.getName());
    private ScheduledFuture myFuture;

    private boolean active = false;
    private int interval = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.gc.interval"));
    private int cycleCount = 0;

    public SystemMonitor(CometThreadManagement mgr) {
        this.myFuture = mgr.executePeriodic(this, this.interval, this.interval, TimeUnit.MINUTES);
        this.active = true;
    }

    @Override
    public void run() {
        while(this.active) {
            try {
                if(!this.active) {
                    return;
                }

                System.gc();
                cycleCount++;

                // Make sure we don't run this process too often as it spikes CPU usage.
                // to modify interval, check comet.properties
                TimeUnit.MINUTES.sleep(interval);
            } catch(Exception e) {
                if(e instanceof InterruptedException) {
                    return;
                }

                log.error("Error while monitoring server", e);
            }
        }
    }

    public void stop() {
        this.active = false;
        this.myFuture.cancel(false);
    }

    public int getCycleCount() {
        return this.cycleCount;
    }
}
