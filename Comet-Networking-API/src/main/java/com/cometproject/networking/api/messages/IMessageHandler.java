package com.cometproject.networking.api.messages;

import com.cometproject.api.networking.messages.IMessageEvent;
import com.cometproject.api.networking.sessions.ISession;

public interface IMessageHandler {
    void handleMessage(final IMessageEvent messageEvent, ISession session);
}
