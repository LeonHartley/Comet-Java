package com.cometproject.process.tasks;

import com.cometproject.process.CometProcessManager;
import com.cometproject.process.processes.AbstractProcess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ProcessWatchdog implements Runnable {

    private final CometProcessManager processManager;

    private final Logger log = LogManager.getLogger(ProcessWatchdog.class);

    public ProcessWatchdog(CometProcessManager processManager) {
        this.processManager = processManager;
    }

    public void run() {
        for(Map.Entry<String, AbstractProcess> process : this.processManager.getProcesses().entrySet()) {
            process.getValue().performStatusCheck();
        }
    }
}
