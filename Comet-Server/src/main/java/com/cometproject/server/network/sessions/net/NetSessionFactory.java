package com.cometproject.server.network.sessions.net;

import com.cometproject.networking.api.sessions.INetSession;
import com.cometproject.networking.api.sessions.INetSessionFactory;
import com.cometproject.server.network.messages.MessageHandler;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.sessions.SessionManager;
import io.netty.channel.ChannelHandlerContext;

public class NetSessionFactory implements INetSessionFactory {
    private final SessionManager sessionManager;
    private final MessageHandler gameMessageHandler;

    public NetSessionFactory(SessionManager sessionManager, MessageHandler gameMessageHandler) {
        this.sessionManager = sessionManager;
        this.gameMessageHandler = gameMessageHandler;
    }

    @Override
    public INetSession createSession(ChannelHandlerContext channel) {
        if (!sessionManager.add(channel)) {
            return null;
        }

        final Session session = channel.attr(SessionManager.SESSION_ATTR).get();

        return new NetSession(this.gameMessageHandler, session);
    }
}
