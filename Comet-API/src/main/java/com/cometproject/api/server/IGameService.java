package com.cometproject.api.server;

import com.cometproject.api.events.EventHandler;
import com.cometproject.api.networking.sessions.ISessionManager;

import java.util.UUID;

public interface IGameService {
    ISessionManager getSessionManager();

    EventHandler getEventHandler();
}
