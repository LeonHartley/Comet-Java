package com.cometproject.process.processes;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractProcess {
    private final String processName;
    private final Logger log;

    private ProcessStatus processStatus;
    private long lastStatusCheck = System.currentTimeMillis();

    public AbstractProcess(String processName) {
        this.processName = processName;
        this.processStatus = ProcessStatus.STARTING;

        this.log = LogManager.getLogger(this.getClass().getName() + "#" + processName);
    }

    public abstract String executionCommand();

    public abstract void statusCheck();

    public JsonObject buildStatusObject() {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", this.getProcessName());
        jsonObject.addProperty("status", this.getProcessStatus().toString());
        jsonObject.addProperty("lastStatusCheck", this.getLastStatusCheck());

        return jsonObject;
    }

    public void performStatusCheck() {
        if (this.requiresStatusCheck()) {
            log.trace("Processing status check for instance");

            if(this.getProcessStatus() == ProcessStatus.STARTING) {
                log.info("Starting instance");
            }

            this.statusCheck();

            this.lastStatusCheck = System.currentTimeMillis();
        }
    }

    public int statusCheckInterval() {
        return 30000;
    }

    public boolean requiresStatusCheck() {
        return System.currentTimeMillis() >= this.lastStatusCheck + this.statusCheckInterval();
    }

    public String getProcessName() {
        return this.processName;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(final ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public long getLastStatusCheck() {
        return this.lastStatusCheck;
    }
}
