package com.cometproject.process.web;

import com.cometproject.process.CometProcessManager;
import com.cometproject.process.web.util.JsonRouteTransformer;
import com.google.gson.Gson;
import spark.Route;
import spark.Spark;

public abstract class AbstractRouteController {
    protected static final Gson gson = new Gson();

    private final CometProcessManager processManager;

    public AbstractRouteController(final CometProcessManager processManager) {
        this.processManager = processManager;
    }

    protected void get(String location, Route route) {
        Spark.get(location, (req, res) -> {
            res.type("application/json");

            return route.handle(req, res);
        }, JsonRouteTransformer.getInstance());
    }

    protected void post(String location, Route route) {
        Spark.post(location, (req, res) -> {
            res.type("application/json");

            return route.handle(req, res);
        }, JsonRouteTransformer.getInstance());
    }


    public abstract void install();

    protected CometProcessManager getProcessManager() {
        return processManager;
    }
}
