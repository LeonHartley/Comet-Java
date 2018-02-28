package com.cometproject.server.network.monitor;


import com.cometproject.api.stats.CometStats;
import com.cometproject.server.boot.Comet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;


public class MonitorMessageLibrary {
    public static boolean isInitialized = false;
    public static String request;
    public static ChannelHandlerContext ctx;
    private static Logger log = Logger.getLogger(MonitorMessageLibrary.class.getName());
    private static Gson gsonInstance = new Gson();

    // Hello message
    public static void hello() {
        isInitialized = true;

        heartbeat();
    }

    public static void heartbeat() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("name", gsonInstance.toJsonTree("status"));
        jsonObject.add("message", gsonInstance.toJsonTree(Comet.getStats(), CometStats.class));

        sendMessage(jsonObject.toString());
    }

    public static void sendMessage(String json) {
        ByteBuf msg = Unpooled.buffer(json.getBytes().length);

        for (int i = 0; i < msg.capacity(); i++) {
            msg.writeByte(json.getBytes()[i]);
        }

        ctx.writeAndFlush(msg);
    }
}
