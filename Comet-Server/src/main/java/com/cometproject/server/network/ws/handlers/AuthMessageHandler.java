package com.cometproject.server.network.ws.handlers;

import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.ws.messages.AuthOKMessage;
import com.cometproject.server.network.ws.messages.AuthRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AuthMessageHandler extends AbstractWsHandler<AuthRequest> {
    public AuthMessageHandler() {
        super(AuthRequest.class);
    }

    @Override
    protected void onMessage(AuthRequest message, ChannelHandlerContext ctx) {
        final Integer playerId = PlayerManager.getInstance().getPlayerIdByAuthToken(message.getSsoTicket());
        if (playerId != null) {
            final Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

            if (session != null) {
                ctx.attr(SESSION).set(session);
                session.setWsChannel(ctx);

                session.sendWs(new AuthOKMessage());
            }
        }
    }
}