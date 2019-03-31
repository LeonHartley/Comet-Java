package com.cometproject.server.network.ws;

import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.network.ws.handlers.*;
import com.cometproject.server.network.ws.request.WsRequest;
import com.cometproject.server.network.ws.request.WsRequestType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WsMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger log = Logger.getLogger(WsMessageHandler.class);

    private static final Map<WsRequestType, WsHandler> handlers = new ConcurrentHashMap<WsRequestType, WsHandler>() {{
        put(WsRequestType.AUTH, new AuthMessageHandler());
        put(WsRequestType.PIANO_PLAY_NOTE, new PlayPianoMessageHandler());
        put(WsRequestType.OPEN_ROOM, new OpenRoomMessageHandler());
        put(WsRequestType.ROOM_VOTE, new RoomVoteMessageHandler());
        put(WsRequestType.OPEN_LINK, new OpenLinkMessageHandler());
    }};

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        try {
            final WsRequest request = JsonUtil.getInstance().fromJson(frame.text(), WsRequest.class);
            final WsHandler handler = handlers.get(request.getType());

            if(handler != null) {
                handler.handle(frame.text(), ctx);
            }
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Error handling websocket msg", e);
        }
    }
}
