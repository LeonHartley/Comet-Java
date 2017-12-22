package com.cometproject.server.network.sessions.net;

import com.cometproject.networking.api.messages.IMessageHandler;
import com.cometproject.networking.api.sessions.INetSession;
import com.cometproject.server.network.sessions.Session;
import io.netty.channel.ChannelHandlerContext;

public class NetSession implements INetSession<Session> {

    private final Session session;

    public NetSession(Session session) {
        this.session = session;
    }

    @Override
    public ChannelHandlerContext getChannel() {
        return this.session.getChannel();
    }

    @Override
    public Session getGameSession() {
        return this.session;
    }
}
