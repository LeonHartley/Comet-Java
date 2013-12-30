package com.cometsrv.network.clients;

import com.cometsrv.boot.Comet;
import com.cometsrv.network.NetworkEngine;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

public class ClientHandler extends SimpleChannelInboundHandler<Event> {
    private static Logger log = Logger.getLogger(ClientHandler.class.getName());

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        //log.debug("Channel [" + ctx.channel().attr(NetworkEngine.UNIQUE_ID_KEY).get().toString() + "] connected");

        if(!Comet.getServer().getNetwork().getSessions().add(ctx.channel())) {
            ctx.channel().disconnect();
        }
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        try {
            Session client = ctx.channel().attr(NetworkEngine.SESSION_ATTRIBUTE_KEY).get();
            client.onDisconnect();
        } catch(Exception e) { }

        Comet.getServer().getNetwork().getSessions().remove(ctx.channel());
        //log.debug("Channel [" + ctx.channel().attr(NetworkEngine.UNIQUE_ID_KEY).get().toString() + "] disconnected");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Event event) throws Exception {
        try {
            Session client = channelHandlerContext.channel().attr(NetworkEngine.SESSION_ATTRIBUTE_KEY).get();

            if(client != null) {
                //System.out.println(o);
                Comet.getServer().getNetwork().getMessages().handle(event, client);
            }
        } catch(Exception e) {
            log.error("Error while recieving message", e);
        }
    }
}
