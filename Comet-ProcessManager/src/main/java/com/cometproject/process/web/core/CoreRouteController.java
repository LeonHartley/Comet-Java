package com.cometproject.process.web.core;

import com.cometproject.process.CometProcessManager;
import com.cometproject.process.processes.AbstractProcess;
import com.cometproject.process.web.AbstractRouteController;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class CoreRouteController extends AbstractRouteController {

    public CoreRouteController(CometProcessManager processManager) {
        super(processManager);
    }

    private Map<String, Object> status(Request request, Response response) {
        final Map<String, Object> result = new HashMap<>();
        final Map<String, JsonObject> processes = new HashMap<>();

        for (AbstractProcess process : this.getProcessManager().getProcesses().values()) {
            processes.put(process.getProcessName(), process.buildStatusObject());
        }

        result.put("status", processes);
        return result;
    }

    public void install() {
        this.get("/version", ((request, response) -> {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("version", "1.0");

            return jsonObject;
        }));

        this.get("/", ((request, response) -> {
            return null;
        }));
        this.get("/status", this::status);
    }
}
