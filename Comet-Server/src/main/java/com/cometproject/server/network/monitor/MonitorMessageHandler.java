package com.cometproject.server.network.monitor;


import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



public class MonitorMessageHandler {
    private List<String> messageRegistry;
    private Logger log = Logger.getLogger(MonitorMessageHandler.class.getName());

    public MonitorMessageHandler() {
        this.messageRegistry = new ArrayList<>();

        this.messageRegistry.add("hello");
        this.messageRegistry.add("heartbeat");
    }

    public boolean handle(MonitorPacket message, ChannelHandlerContext ctx) {
        String messageHeader = message.getName();

        if (!this.messageRegistry.contains(messageHeader)) {
            return false;
        }

        try {
            MonitorMessageLibrary.request = message.getMessage();
            MonitorMessageLibrary.ctx = ctx;

            Method method = MonitorMessageLibrary.class.getMethod(messageHeader);
            method.invoke(new Object[0]);
        } catch (Exception e) {
            log.error("Error while handling monitor packet", e);
            return false;
        }

        return true;
    }
}
