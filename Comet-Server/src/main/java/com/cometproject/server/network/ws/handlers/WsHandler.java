package com.cometproject.server.network.ws.handlers;

import io.netty.channel.ChannelHandlerContext;

public interface WsHandler {
    void handle(String data, ChannelHandlerContext ctx);
}
