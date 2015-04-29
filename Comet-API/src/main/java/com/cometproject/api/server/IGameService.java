package com.cometproject.api.server;

import com.cometproject.api.events.EventHandler;
import com.cometproject.api.networking.sessions.ISessionManager;

public interface IGameService {
    ISessionManager getSessionManager();

    EventHandler getEventHandler();
}
