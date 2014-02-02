package com.cometsrv.network.monitor;

import io.netty.channel.ChannelHandlerContext;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.List;

public class MonitorMessageHandler {
    private List<String> messageRegistry;
    private Logger log = Logger.getLogger(MonitorMessageHandler.class.getName());

    public MonitorMessageHandler() {
        this.messageRegistry = new FastList<>();
        this.messageRegistry.add("hello");
    }

    public boolean handle(String originalMessage, ChannelHandlerContext ctx) {
        String messageHeader = originalMessage.split(":")[0];

        if(!this.messageRegistry.contains(messageHeader)) {
            return false;
        }

        try {
            MonitorMessageLibrary.request = originalMessage.replace(messageHeader + ":", "");
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
