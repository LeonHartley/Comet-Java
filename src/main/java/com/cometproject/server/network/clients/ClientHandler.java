package com.cometproject.server.network.clients;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.NetworkEngine;
import com.cometproject.server.network.messages.outgoing.misc.PingMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler extends SimpleChannelInboundHandler<Event> {
    private static Logger log = Logger.getLogger(ClientHandler.class.getName());
    private Map<String, AtomicInteger> connectionsPerIp = new FastMap<>();

    private final int CONNECTIONS_PER_IP = Integer.parseInt(Comet.getServer().getConfig().get("comet.network.connPerIp"));

    public ClientHandler() {
        super(true); // auto release byte bufs
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Event event) throws Exception {
        try {
            Session session = channelHandlerContext.channel().attr(NetworkEngine.SESSION_ATTR).get();

            if (session != null) {
                Comet.getServer().getNetwork().getMessages().handle(event, session);
            }
        } catch (Exception e) {
            log.error("Error while receiving message", e);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (!Comet.getServer().getNetwork().getSessions().add(channelHandlerContext.channel())) {
            channelHandlerContext.channel().disconnect();
            return;
        }

        /*String ip = ((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress();

        if (CONNECTIONS_PER_IP != 0) {
            if (this.connectionsPerIp.containsKey(ip)) {
                int connectionCount = connectionsPerIp.get(ip).incrementAndGet();
                if (connectionCount > CONNECTIONS_PER_IP) { channelHandlerContext.channel().disconnect(); }
            } else {
                this.connectionsPerIp.put(ip, new AtomicInteger());
            }
        }*/

        super.channelActive(channelHandlerContext);
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        try {
            Session session = channelHandlerContext.channel().attr(NetworkEngine.SESSION_ATTR).get();
            session.onDisconnect();
        } catch (Exception e) { }

        Comet.getServer().getNetwork().getSessions().remove(channelHandlerContext.channel());

        /*String ip = ((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress();

        if (CONNECTIONS_PER_IP != 0) {
            if (this.connectionsPerIp.containsKey(ip)) {
                int connectionCount = connectionsPerIp.get(ip).get();

                if (connectionCount == 1)
                    this.connectionsPerIp.remove(ip);
            } else {
                this.connectionsPerIp.get(ip).decrementAndGet();
            }
        }*/

        super.channelInactive(channelHandlerContext);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                channelHandlerContext.channel().disconnect();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                channelHandlerContext.channel().writeAndFlush(PingMessageComposer.compose());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        if (channelHandlerContext.channel().isActive()) {
            if (cause instanceof IOException) {
                channelHandlerContext.channel().disconnect();
                return;
            }

            log.error("Exception in ClientHandler : " + cause.getMessage());

            cause.printStackTrace();
            channelHandlerContext.channel().disconnect();
        }
    }
}
