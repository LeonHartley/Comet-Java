package com.cometproject.server.network.clients;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler extends SimpleChannelUpstreamHandler {
    private static Logger log = Logger.getLogger(ClientHandler.class.getName());
    private Map<String, AtomicInteger> connectionsPerIp = new FastMap<>();

    private final boolean CLOSE_ON_ERROR = false;
    private final int CONNECTIONS_PER_IP = Integer.parseInt(Comet.getServer().getConfig().get("comet.network.connPerIp"));

    @Override
    public void channelOpen(final ChannelHandlerContext ctx, ChannelStateEvent ev) throws Exception {
        if (!Comet.getServer().getNetwork().getSessions().add(ctx.getChannel())) {
            ctx.getChannel().disconnect();
        }

        String ip = ((InetSocketAddress)ctx.getChannel().getRemoteAddress()).getAddress().getHostAddress();

        if (CONNECTIONS_PER_IP != 0) {
            if (this.connectionsPerIp.containsKey(ip)) {
                int connectionCount = connectionsPerIp.get(ip).incrementAndGet();

                if (connectionCount > CONNECTIONS_PER_IP)
                    ctx.getChannel().close();
            } else {
                this.connectionsPerIp.put(ip, new AtomicInteger());
            }
        }

        super.channelOpen(ctx, ev);
    }

    @Override
    public void channelClosed(final ChannelHandlerContext ctx, ChannelStateEvent ev) throws Exception {
        try {
            Session client = (Session) ctx.getChannel().getAttachment();
            client.onDisconnect();
        } catch (Exception e) {
        }

        Comet.getServer().getNetwork().getSessions().remove(ctx.getChannel());
        log.debug("Channel [" + ctx.getChannel().getId() + "] disconnected");

        String ip = ((InetSocketAddress)ctx.getChannel().getRemoteAddress()).getAddress().getHostAddress();

        if (CONNECTIONS_PER_IP != 0) {
            if (this.connectionsPerIp.containsKey(ip)) {
                int connectionCount = connectionsPerIp.get(ip).get();

                if (connectionCount == 1)
                    this.connectionsPerIp.remove(ip);
            } else {
                this.connectionsPerIp.get(ip).decrementAndGet();
            }
        }

        super.channelClosed(ctx, ev);
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, MessageEvent ev) throws Exception {
        try {
            Session client = (Session) ctx.getChannel().getAttachment();

            if (client != null && (ev.getMessage() instanceof Event)) {
                Comet.getServer().getNetwork().getMessages().handle((Event) ev.getMessage(), client);
            }
        } catch (Exception e) {
            log.error("Error while receiving message", e);
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, ExceptionEvent ev) throws Exception {
        if (ctx.getChannel().isConnected()) {
            if (ev.getCause() instanceof IOException) {
                ctx.getChannel().close();
                return;
            }

            log.error("Exception in ClientHandler : " + ev.getCause().getMessage());

            ev.getCause().printStackTrace();

            if (CLOSE_ON_ERROR) {
                ctx.getChannel().close();
            }
        }
    }
}
