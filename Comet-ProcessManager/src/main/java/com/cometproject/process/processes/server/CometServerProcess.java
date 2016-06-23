package com.cometproject.process.processes.server;

import com.cometproject.process.processes.AbstractProcess;
import com.cometproject.process.processes.ProcessStatus;
import com.google.gson.JsonObject;

public class CometServerProcess extends AbstractProcess {

    private final String applicationArguments;
    private final String serverVersion;

    private final String apiUrl;
    private final String apiToken;

    public CometServerProcess(final String instanceName, final String applicationArguments, final String serverVersion, final String apiUrl, final String apiToken) {
        super(instanceName);

        this.applicationArguments = applicationArguments;
        this.serverVersion = serverVersion;
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
    }

    @Override
    public String[] executionCommand() {
        return new String[]{"java", "-jar", System.getProperty("user.dir") + "/Versions/" + this.serverVersion + "/Comet-Server-" + this.serverVersion + ".jar", this.applicationArguments, "--instance-name=" + this.getProcessName(), "--daemon"};
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