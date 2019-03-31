package com.cometproject.server.network.ws.handlers;

import com.cometproject.server.network.messages.outgoing.misc.OpenLinkMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.ws.messages.OpenLinkRequest;
import io.netty.channel.ChannelHandlerContext;

public class OpenLinkMessageHandler extends AbstractWsHandler<OpenLinkRequest> {
    public OpenLinkMessageHandler() {
        super(OpenLinkRequest.class);
    }

    @Override
    protected void onMessage(OpenLinkRequest message, ChannelHandlerContext ctx) {
        final Session session = ctx.attr(SESSION).get();

        if (session.getPlayer() != null) {
            session.send(new OpenLinkMessageComposer(message.getLink()));
        }
    }
}
