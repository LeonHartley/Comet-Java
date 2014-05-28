package com.cometproject.server.network.clients;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.NetworkEngine;
import com.cometproject.server.network.messages.outgoing.misc.PingMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ClientHandler extends SimpleChannelInboundHandler<Event> {
    private static Logger log = Logger.getLogger(ClientHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);

        log.trace("Channel read called");

        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;

            if (buf.refCnt() != 0) {
                throw new Exception("Buffer was not released");
            }
        }
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

        super.channelActive(channelHandlerContext);
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        try {
            Session session = channelHandlerContext.channel().attr(NetworkEngine.SESSION_ATTR).get();
            session.onDisconnect();
        } catch (Exception e) { }

        Comet.getServer().getNetwork().getSessions().remove(channelHandlerContext.channel());

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
