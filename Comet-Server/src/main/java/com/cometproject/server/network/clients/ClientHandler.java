package com.cometproject.server.network.clients;

import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.misc.PingMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.sessions.SessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<MessageEvent> {
    private static Logger log = Logger.getLogger(ClientHandler.class.getName());

    private static ClientHandler clientHandlerInstance;

    private AtomicInteger activeConnections = new AtomicInteger(0);

    public static ClientHandler getInstance() {
        if (clientHandlerInstance == null)
            clientHandlerInstance = new ClientHandler();

        return clientHandlerInstance;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.activeConnections.incrementAndGet();

        if (!NetworkManager.getInstance().getSessions().add(ctx)) {
            ctx.channel().disconnect();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.activeConnections.decrementAndGet();

        try {
            Session session = ctx.attr(SessionManager.SESSION_ATTR).get();
            session.onDisconnect();
        } catch (Exception e) {
//            e.printStackTrace();
        }

        NetworkManager.getInstance().getSessions().remove(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (NetworkManager.IDLE_TIMER_ENABLED) {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                if (e.state() == IdleState.READER_IDLE) {
                    ctx.close();
                } else if (e.state() == IdleState.WRITER_IDLE) {
                    ctx.writeAndFlush(new PingMessageComposer());
                }
            }
        }

        if (evt instanceof ChannelInputShutdownEvent) {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            ctx.close();
        }

        if (cause instanceof IOException) return;

        log.error("Exception caught in ClientHandler", cause);
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, MessageEvent event) throws Exception {
        try {
            Session session = channelHandlerContext.attr(SessionManager.SESSION_ATTR).get();

            if (session != null) {
                session.handleMessageEvent(event);
            }
        } catch (Exception e) {
            log.error("Error while receiving message", e);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    public AtomicInteger getActiveConnections() {
        return activeConnections;
    }
}