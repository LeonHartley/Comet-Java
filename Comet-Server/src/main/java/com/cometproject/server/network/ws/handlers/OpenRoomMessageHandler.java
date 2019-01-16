package com.cometproject.server.network.ws.handlers;

import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.ws.messages.OpenRoomRequest;
import io.netty.channel.ChannelHandlerContext;

public class OpenRoomMessageHandler extends AbstractWsHandler<OpenRoomRequest> {
    public OpenRoomMessageHandler() {
        super(OpenRoomRequest.class);
    }

    @Override
    protected void onMessage(OpenRoomRequest message, ChannelHandlerContext ctx) {
        final Session session = ctx.attr(SESSION).get();

        if (session != null) {
            session.send(new RoomForwardMessageComposer(message.getRoomId()));
        }
    }
}
