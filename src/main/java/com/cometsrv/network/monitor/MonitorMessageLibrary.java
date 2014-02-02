package com.cometsrv.network.monitor;

import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

public class MonitorMessageLibrary {
    private static Logger log = Logger.getLogger(MonitorMessageLibrary.class.getName());

    public static String request;
    public static ChannelHandlerContext ctx;

    // Hello message
    public static void hello() {

        log.debug(request);
    }
}
