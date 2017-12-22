package com.cometproject.networking.api.sessions;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public interface INetSessionFactory {

    INetSession createSession(ChannelHandlerContext channel);

    void disposeSession(INetSession session);

}
