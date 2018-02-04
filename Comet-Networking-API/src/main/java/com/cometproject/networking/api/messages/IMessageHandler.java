package com.cometproject.networking.api.messages;

import com.cometproject.api.networking.messages.IMessageEvent;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.networking.api.sessions.INetSession;

public interface IMessageHandler<T extends INetSession> {
    void handleMessage(final IMessageEvent messageEvent, T session);
}
