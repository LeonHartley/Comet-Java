package com.cometproject.process.web.processes;

import com.cometproject.process.CometProcessManager;
import com.cometproject.process.processes.AbstractProcess;
import com.cometproject.process.processes.server.CometServerProcess;
import com.cometproject.process.web.AbstractRouteController;
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

                process = new CometServerProcess(processName, applicationArguments, serverVersion);
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

    @Override
    public void install() {
        this.post("/process/start/:type", this::start);
    }
}
