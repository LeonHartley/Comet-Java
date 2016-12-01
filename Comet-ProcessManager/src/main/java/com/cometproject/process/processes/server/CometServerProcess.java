package com.cometproject.process.processes.server;

import com.cometproject.process.api.CometAPIClient;
import com.cometproject.process.processes.AbstractProcess;
import com.cometproject.process.processes.ProcessStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.http.HttpMethod;

public class CometServerProcess extends AbstractProcess {

    private static final Gson gson = new Gson();

    private String applicationArguments;
    private String serverVersion;

    private final String apiUrl;
    private final String apiToken;

    private JsonObject statusObject = null;

    public CometServerProcess(final String instanceName, final String applicationArguments, final String serverVersion,
                              final String apiUrl, final String apiToken) {
        super(instanceName);

        this.applicationArguments = applicationArguments;
        this.serverVersion = serverVersion;
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
    }

    @Override
    public String[] executionCommand() {
        return new String[]{
                "java",
                "-Dfile.encoding=UTF-8",
                "-jar",
                System.getProperty("user.dir") + "/Versions/" + this.serverVersion +
                        "/Comet-Server-" + this.serverVersion + ".jar", this.applicationArguments,

                "--instance-name=" + this.getProcessName(),
                "--daemon"};
    }

    @Override
    public int statusCheckInterval() {
        return 5000;
    }

    @Override
    public void statusCheck() {
        if (this.getProcessStatus() == ProcessStatus.UP) {
            // Make a status request to the process' http API and grab status object
            try {
                this.statusObject = gson.fromJson(CometAPIClient.getInstance().submitRequest(HttpMethod.GET,
                        this.apiToken, this.apiUrl + "/system/status", null), JsonObject.class);
            } catch (Exception e) {
                // Failed status check, WARNING!
                this.setProcessStatus(ProcessStatus.WARNING);
            }
        }

        // Check for any issues with this specific instance.
        //this.setProcessStatus(ProcessStatus.UP);
    }

    @Override
    public JsonObject buildStatusObject() {
        final JsonObject obj = super.buildStatusObject();

        obj.addProperty("serverVersion", this.serverVersion);
        obj.add("serverStatus", this.statusObject);

        return obj;
    }

    public String getApplicationArguments() {
        return applicationArguments;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public JsonObject getStatusObject() {
        return statusObject;
    }

    public void setApplicationArguments(String applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }
}