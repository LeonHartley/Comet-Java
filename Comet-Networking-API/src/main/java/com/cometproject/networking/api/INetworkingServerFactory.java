package com.cometproject.networking.api;

import com.cometproject.api.networking.sessions.ISessionService;
import com.cometproject.networking.api.config.NetworkingServerConfig;
import com.cometproject.networking.api.messages.IMessageHandler;

public interface INetworkingServerFactory {

    INetworkingServer createServer(NetworkingServerConfig serverConfig, IMessageHandler messageHandler,
                                   ISessionService sessionService);

}
