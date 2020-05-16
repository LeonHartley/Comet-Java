package com.cometproject.networking.api;

import com.cometproject.networking.api.config.NetworkingServerConfig;
import com.cometproject.networking.api.sessions.INetSessionFactory;

public interface INetworkingServerFactory {

    INetworkingServer createServer(NetworkingServerConfig serverConfig, INetSessionFactory sessionFactory);

}
