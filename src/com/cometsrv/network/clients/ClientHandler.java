package com.cometsrv.network.clients;

import com.cometsrv.boot.Comet;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;

public class ClientHandler extends SimpleChannelHandler {

    private static Logger log = Logger.getLogger(ClientHandler.class.getName());

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent event) {
        log.debug("Channel [" + ctx.getChannel().getId() + "] connected");

        if(!Comet.getServer().getNetwork().getSessions().add(ctx.getChannel())) {
            ctx.getChannel().disconnect();
        }
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent event) {
        try {
            Session client = (Session) ctx.getChannel().getAttachment();
            client.onDisconnect();
        } catch(Exception e) { }

        Comet.getServer().getNetwork().getSessions().remove(ctx.getChannel());
        log.debug("Channel [" + ctx.getChannel().getId() + "] disconnected");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) {
        try {
            Session session = (Session) ctx.getChannel().getAttachment();

            if(session != null) {
                Comet.getServer().getNetwork().getMessages().handle((Event) event.getMessage(), session);
            }
        } catch(Exception e) {
            log.error("Error while recieving message", e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        //if(e instanceof ClosedChannelException) {
        //  return;
        //}

        //log.error("Exception caught from channel handler", e.getCause());
    }
}
