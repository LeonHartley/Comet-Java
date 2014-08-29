package com.cometproject.server.logging.appenders;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.monitor.MonitorClientHandler;
import com.cometproject.server.network.monitor.MonitorMessageLibrary;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class MonitorAppender extends AppenderSkeleton {
    private Gson gson = new Gson();

    public MonitorAppender() {

    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if(!MonitorMessageLibrary.isInitialized) {
            // Comet isn't finished booting up yet.
            return;
        }

        MonitorClientHandler clientHandler = Comet.getServer().getNetwork().getMonitorClient().getClientHandler();

        if(clientHandler == null)
            return;

        JsonObject jsonObject = new JsonObject();

        jsonObject.add("name", gson.toJsonTree("appendLog"));
        jsonObject.add("message", gson.toJsonTree(loggingEvent, LoggingEvent.class));

        MonitorMessageLibrary.sendMessage(clientHandler.getContext(), jsonObject.toString());
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
