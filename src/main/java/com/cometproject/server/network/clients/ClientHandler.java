package com.cometproject.server.network.clients;

import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.sessions.SessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<Event> {
    private static Logger log = Logger.getLogger(ClientHandler.class.getName());

    private static ClientHandler clientHandlerInstance;

    public static ClientHandler getInstance() {
        if(clientHandlerInstance == null)
            clientHandlerInstance = new ClientHandler();

        return clientHandlerInstance;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!NetworkManager.getInstance().getSessions().add(ctx)) {
            ctx.channel().disconnect();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            Session session = ctx.attr(SessionManager.SESSION_ATTR).get();
            session.onDisconnect();
        } catch (Exception e) {

        }

        NetworkManager.getInstance().getSessions().remove(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            ctx.close();
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Event event) throws Exception {
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
}