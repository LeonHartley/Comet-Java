package com.cometproject.networking.api.sessions;

import com.cometproject.networking.api.messages.IMessageHandler;

public interface INetSession<T> {

    IMessageHandler getMessageHandler();

    T getGameSession();

}
