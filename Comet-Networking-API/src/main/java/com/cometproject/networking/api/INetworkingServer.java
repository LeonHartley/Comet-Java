package com.cometproject.networking.api;

import com.cometproject.networking.api.config.NetworkingServerConfig;
import com.cometproject.api.networking.sessions.ISessionService;
import com.cometproject.networking.api.messages.IMessageHandler;
import com.cometproject.networking.api.sessions.INetSessionFactory;

public interface INetworkingServer {

    void start();

    NetworkingServerConfig getServerConfig();

    INetSessionFactory getSessionFactory();
}
