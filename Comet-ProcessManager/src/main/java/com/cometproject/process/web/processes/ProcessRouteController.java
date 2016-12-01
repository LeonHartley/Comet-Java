package com.cometproject.process.web.processes;

import com.cometproject.process.CometProcessManager;
import com.cometproject.process.processes.AbstractProcess;
import com.cometproject.process.processes.ProcessStatus;
import com.cometproject.process.processes.server.CometServerProcess;
import com.cometproject.process.web.AbstractRouteController;
import com.corundumstudio.socketio.SocketIOClient;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ProcessRouteController extends AbstractRouteController {
    public ProcessRouteController(CometProcessManager processManager) {
        super(processManager);
    }

    public Map<String, Object> start(Request request, Response response) {
        final Map<String, Object> result = new HashMap<>();

        final String processType = request.params("type");
        final String processName = request.queryParams("name");

        AbstractProcess process = null;

        switch(processType) {
            case "CometServer":
                // Create a CometServer instance.
                final String applicationArguments = request.queryParams("applicationArguments");
                final String serverVersion = request.queryParams("serverVersion");
                final String apiUrl = request.queryParams("apiUrl");
                final String apiToken = request.queryParams("apiToken");

                process = new CometServerProcess(processName, applicationArguments, serverVersion, apiUrl, apiToken);

                final AbstractProcess oldProcess = this.getProcessManager().getProcesses().get(processName);

                if(oldProcess != null) {
                    for(SocketIOClient client : oldProcess.getListeners()) {
                        process.listen(client);
                    }
                }

                break;
        }

        if(process != null) {
            this.getProcessManager().getProcesses().put(processName, process);
            result.put("status", "OK");
        } else {
            result.put("status", "FAIL");
        }

        return result;
    }

    public Map<String, Object> status(Request request, Response response) {
        final Map<String, Object> result = new HashMap<>();

        final String processId = request.params("processId");

        final AbstractProcess process = this.getProcessManager().getProcesses().get(processId);

        if(process == null) {
            result.put("status", ProcessStatus.DOWN);
            return result;
        }

        result.put("status", process.getProcessStatus());
        result.put("statusObject", process.buildStatusObject());

        return result;
    }

    public Map<String, Object> stop(Request request, Response response) {
        final Map<String, Object> result = new HashMap<>();

        final String processId = request.params("processId");

        final AbstractProcess process = this.getProcessManager().getProcesses().get(processId);

        if(process == null) {
            result.put("status", ProcessStatus.DOWN);
            return result;
        }

        process.setProcessStatus(ProcessStatus.STOPPING);
        result.put("status", ProcessStatus.STOPPING);

        return result;
    }

    @Override
    public void install() {
        this.post("/process/start/:type", this::start);
        this.get("/process/stop/:processId", this::stop);
        this.get("/process/status/:processId", this::status);
    }
}
