package com.cometproject.networking.api.sessions;

import com.cometproject.networking.api.messages.IMessageHandler;
import io.netty.channel.ChannelHandlerContext;

public interface INetSession<T> {

    ChannelHandlerContext getChannel();

    IMessageHandler getMessageHandler();

    T getGameSession();

}
