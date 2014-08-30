package com.cometproject.server.logging.appenders;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.monitor.MonitorMessageLibrary;
import com.google.gson.Gson;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class MonitorAppender extends AppenderSkeleton {
    private Gson gson = new Gson();

    public MonitorAppender() {

    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if(Comet.getServer() == null || Comet.getServer().getNetwork() == null || Comet.getServer().getNetwork().getMonitorClient() == null || !MonitorMessageLibrary.isInitialized) {
            // Comet isn't finished booting up yet.
            return;
        }

        //        MonitorClientHandler clientHandler = Comet.getServer().getNetwork().getMonitorClient().getClientHandler();
//
//        if(clientHandler == null)
//            return;
//
//        JsonObject jsonObject = new JsonObject();
//        JsonObject logObject = new JsonObject();
//
//        logObject.add("name", gson.toJsonTree(loggingEvent.getLoggerName()));
//        logObject.add("message", gson.toJsonTree(loggingEvent.getRenderedMessage()));
//        logObject.add("time", gson.toJsonTree(loggingEvent.getTimeStamp()));
//        logObject.add("level", gson.toJsonTree(loggingEvent.getLevel().toString().toLowerCase()));
//
//        jsonObject.add("name", gson.toJsonTree("appendLog"));
//        jsonObject.add("message", logObject);
//
//        MonitorMessageLibrary.sendMessage(clientHandler.getContext(), jsonObject.toString());
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
