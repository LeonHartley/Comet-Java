package com.cometproject.server.network.ws;

import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WsMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    enum RequestType {
        AUTH
    }

    class Request {
        private final RequestType type;
        private final String data;

        public Request(RequestType type, String data) {
            this.type = type;
            this.data = data;
        }

        public RequestType getType() {
            return type;
        }

        public String getData() {
            return data;
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String data = frame.text();
        final Request request = JsonUtil.getInstance().fromJson(data, Request.class);

        switch (request.getType()) {
            case AUTH:
                final Integer playerId = PlayerManager.getInstance().getPlayerIdByAuthToken(request.getData());

                if (playerId != null) {
                    final Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

                    if (session != null) {
                        session.setWsChannel(ctx);
                    }
                }
                break;
        }
    }
}
