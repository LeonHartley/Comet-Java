package com.cometproject.process;


import com.cometproject.process.io.WebSocketServer;
import com.cometproject.process.processes.AbstractProcess;
import com.cometproject.process.tasks.ProcessWatchdog;
import com.cometproject.process.web.core.CoreRouteController;
import com.cometproject.process.web.processes.ProcessRouteController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CometProcessManager {
    private final ScheduledExecutorService executorService;

    private final Map<String, AbstractProcess> processes;

    private WebSocketServer webSocketServer;

    private CometProcessManager() {
        this.executorService = Executors.newScheduledThreadPool(4);
        this.processes = new ConcurrentHashMap<>();
    }

    private void initialise() {
        this.executorService.scheduleAtFixedRate(new ProcessWatchdog(this), 1, 1, TimeUnit.SECONDS);

        new CoreRouteController(this).install();
        new ProcessRouteController(this).install();

        this.webSocketServer = new WebSocketServer(this);
    }

    public static void main(String[] args) {
        final CometProcessManager processManager = new CometProcessManager();

        processManager.initialise();
    }

    public Map<String, AbstractProcess> getProcesses() {
        return processes;
    }
}
