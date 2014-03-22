package com.cometproject.server.network.clients;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.NetworkEngine;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler extends SimpleChannelInboundHandler<Event> {
    private static Logger log = Logger.getLogger(ClientHandler.class.getName());
    private Map<String, AtomicInteger> connectionsPerIp = new FastMap<>();

    private final boolean CLOSE_ON_ERROR = false;
    private final int CONNECTIONS_PER_IP = Integer.parseInt(Comet.getServer().getConfig().get("comet.network.connPerIp"));

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        //log.debug("Channel [" + ctx.channel().attr(NetworkEngine.UNIQUE_ID_KEY).get().toString() + "] connected");

        if (!Comet.getServer().getNetwork().getSessions().add(ctx.channel())) {
            ctx.channel().disconnect();
        }

        String ip = ctx.channel().remoteAddress().toString().replace("/", "").split(":")[0];

        if (CONNECTIONS_PER_IP != 0) {
            if (this.connectionsPerIp.containsKey(ip)) {
                int connectionCount = connectionsPerIp.get(ip).incrementAndGet();

                if (connectionCount > CONNECTIONS_PER_IP)
                    ctx.close();
            } else {
                this.connectionsPerIp.put(ip, new AtomicInteger());
            }
        }

        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        try {
            Session client = ctx.channel().attr(NetworkEngine.SESSION_ATTRIBUTE_KEY).get();
            client.onDisconnect();
        } catch (Exception e) {
        }

        Comet.getServer().getNetwork().getSessions().remove(ctx.channel());
        log.debug("Channel [" + ctx.channel().attr(NetworkEngine.UNIQUE_ID_KEY).get().toString() + "] disconnected");

        String ip = ctx.channel().remoteAddress().toString().replace("/", "").split(":")[0];

        if (CONNECTIONS_PER_IP != 0) {
            if (this.connectionsPerIp.containsKey(ip)) {
                int connectionCount = connectionsPerIp.get(ip).get();

                if (connectionCount == 1)
                    this.connectionsPerIp.remove(ip);
            } else {
                this.connectionsPerIp.get(ip).decrementAndGet();
            }
        }

        ctx.fireChannelInactive();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, Event msg) throws Exception {
        try {
            Session client = ctx.channel().attr(NetworkEngine.SESSION_ATTRIBUTE_KEY).get();

            if (client != null) {
                Comet.getServer().getNetwork().getMessages().handle(msg, client);
            }
        } catch (Exception e) {
            log.error("Error while receiving message", e);
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            if(cause instanceof IOException) {
                ctx.close();
                return;
            }

            log.error("Exception in ClientHandler : " + cause.getMessage());

            cause.printStackTrace();

            if (CLOSE_ON_ERROR) {
                ctx.close();
            }
        }
    }
}
