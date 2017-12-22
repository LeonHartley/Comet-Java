package com.cometproject.server.network.sessions.net;

import com.cometproject.networking.api.messages.IMessageHandler;
import com.cometproject.networking.api.sessions.INetSession;
import com.cometproject.server.network.sessions.Session;

public class NetSession implements INetSession<Session> {

    private final IMessageHandler messageHandler;
    private final Session session;

    public NetSession(final IMessageHandler messageHandler, Session session) {
        this.messageHandler = messageHandler;
        this.session = session;
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
