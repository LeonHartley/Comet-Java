package com.cometproject.server.network.ws.handlers;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.ws.messages.piano.PlayPianoMessage;
import io.netty.channel.ChannelHandlerContext;

public class PlayPianoMessageHandler extends AbstractWsHandler<PlayPianoMessage> {
    public PlayPianoMessageHandler() {
        super(PlayPianoMessage.class);
    }

    @Override
    protected void onMessage(PlayPianoMessage message, ChannelHandlerContext ctx) {
        final Session session = ctx.attr(SESSION).get();

        if (session.getPlayer() != null && session.getPlayer().getEntity() != null) {
            final Room room = session.getPlayer().getEntity().getRoom();

            if(room != null) {
                room.getEntities().broadcastWs(message);
            }
        }
    }
}
