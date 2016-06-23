package com.cometproject.process.processes.server;

import com.cometproject.process.processes.AbstractProcess;
import com.cometproject.process.processes.ProcessStatus;
import com.google.gson.JsonObject;

public class CometServerProcess extends AbstractProcess {

    private final String applicationArguments;
    private final String serverVersion;

    public CometServerProcess(final String instanceName, final String applicationArguments, final String serverVersion) {
        super(instanceName);

        this.applicationArguments = applicationArguments;
        this.serverVersion = serverVersion;
    }

    @Override
    public String executionCommand() {
        return "java -jar Comet-Server-" + this.serverVersion + ".jar " + this.applicationArguments + " --instance-name=" + this.getProcessName();
    }

    @Override
    public int statusCheckInterval() {
        return 5000;
    }

    @Override
    public void statusCheck() {
        // Check for any issues with this specific instance.
        this.setProcessStatus(ProcessStatus.UP);
    }

    @Override
    public JsonObject buildStatusObject() {
        final JsonObject obj = super.buildStatusObject();

        obj.addProperty("serverVersion", this.serverVersion);

        return obj;
    }
}
