package com.cometproject.server.network.sessions.net;

import com.cometproject.networking.api.messages.IMessageHandler;
import com.cometproject.networking.api.sessions.INetSession;
import com.cometproject.server.network.sessions.Session;
import io.netty.channel.ChannelHandlerContext;

public class NetSession implements INetSession<Session> {

    private final Session session;
    private final IMessageHandler messageHandler;

    public NetSession(Session session, IMessageHandler messageHandler) {
        this.session = session;
        this.messageHandler = messageHandler;
    }

    @Override
    public ChannelHandlerContext getChannel() {
        return this.session.getChannel();
    }

    @Override
    public IMessageHandler getMessageHandler() {
        return this.messageHandler;
    }

    @Override
    public Session getGameSession() {
        return this.session;
    }
}
