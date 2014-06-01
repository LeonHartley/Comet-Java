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
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ClientHandler extends SimpleChannelInboundHandler<Event> {
    private static Logger log = Logger.getLogger(ClientHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Event msg) throws Exception {
        try {
            Session session = ctx.channel().attr(NetworkEngine.SESSION_ATTR).get();

            if (session != null) {
                Comet.getServer().getNetwork().getMessages().handle(msg, session);
            }
        } catch (Exception e) {
            log.error("Error while receiving message", e);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!Comet.getServer().getNetwork().getSessions().add(ctx.channel())) {
            ctx.channel().disconnect();
            return;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            Session session = ctx.channel().attr(NetworkEngine.SESSION_ATTR).get();
            session.onDisconnect();
        } catch (Exception e) { }

        Comet.getServer().getNetwork().getSessions().remove(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                log.error("Client disconnected for being idle");
                ctx.channel().disconnect();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                ctx.channel().writeAndFlush(PingMessageComposer.compose());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            if (cause instanceof IOException) {
                return;
            }

            log.error("Exception in ClientHandler : " + cause.getMessage());
            cause.printStackTrace();
        }
    }
}
