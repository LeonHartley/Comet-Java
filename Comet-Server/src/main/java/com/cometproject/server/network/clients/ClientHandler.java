package com.cometproject.server.network.clients;

import com.cometproject.networking.api.sessions.INetSession;
import com.cometproject.networking.api.sessions.INetSessionFactory;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.misc.PingMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;

import java.io.IOException;

@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<MessageEvent> {
    private static final AttributeKey<INetSession> ATTR_SESSION = AttributeKey.newInstance("NetSession");

    private static Logger log = Logger.getLogger(ClientHandler.class.getName());

    private static ClientHandler clientHandlerInstance;
    private final INetSessionFactory sessionFactory;

    public ClientHandler(INetSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static ClientHandler getInstance(INetSessionFactory netSessionFactory) {
        if (clientHandlerInstance == null)
            clientHandlerInstance = new ClientHandler(netSessionFactory);

        return clientHandlerInstance;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        final INetSession session = this.sessionFactory.createSession(ctx);

        ctx.attr(ATTR_SESSION).set(session);
        if (session == null) {
            ctx.disconnect();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (ctx.attr(ATTR_SESSION).get() == null) {
            return;
        }

        try {
            INetSession session = ctx.attr(ATTR_SESSION).get();

            this.sessionFactory.disposeSession(session);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        ctx.attr(ATTR_SESSION).remove();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (NetworkManager.IDLE_TIMER_ENABLED) {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                if (e.state() == IdleState.READER_IDLE) {
                    ctx.close();
                } else if (e.state() == IdleState.WRITER_IDLE) {
                    ctx.writeAndFlush(new PingMessageComposer(), ctx.voidPromise());
                }
            }
        }

        if (evt instanceof ChannelInputShutdownEvent) {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (ctx.channel().isActive()) {
            ctx.close();
        }

        if (cause instanceof IOException) return;

        log.error("Exception caught in ClientHandler", cause);
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, MessageEvent event) {
        try {
            final INetSession session = channelHandlerContext.attr(ATTR_SESSION).get();

            if (session != null) {
                session.getMessageHandler().handleMessage(event, session);
            }
        } catch (Exception e) {
            log.error("Error while receiving message", e);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }
}