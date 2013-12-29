package com.cometsrv.monitor;

import com.cometsrv.boot.Comet;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class SystemMonitor implements Runnable {

    private Thread monitorThread;
    private boolean active = false;
    private Logger log = Logger.getLogger(SystemMonitor.class.getName());
    private int interval = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.gc.interval"));
    private int cycleCount = 0;

    public SystemMonitor() {
        this.monitorThread = new Thread(this);
        this.active = true;
        this.monitorThread.start();
    }

    @Override
    public void run() {
        while(this.active) {
            try {
                if(this.monitorThread.isInterrupted() || !this.active) {
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
        this.monitorThread.interrupt();
        this.active = false;
    }

    public int getCycleCount() {
        return this.cycleCount;
    }
}
