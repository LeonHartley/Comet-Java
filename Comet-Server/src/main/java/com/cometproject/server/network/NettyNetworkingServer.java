package com.cometproject.server.network;

import com.cometproject.api.networking.sessions.ISessionService;
import com.cometproject.networking.api.INetworkingServer;
import com.cometproject.networking.api.config.NetworkingServerConfig;
import com.cometproject.networking.api.messages.IMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

public class NettyNetworkingServer implements INetworkingServer {
    private static final Logger log = Logger.getLogger(NettyNetworkingServer.class);

    private final NetworkingServerConfig serverConfig;
    private final IMessageHandler messageHandler;
    private final ISessionService sessionService;
    private final ServerBootstrap serverBootstrap;

    public NettyNetworkingServer(NetworkingServerConfig serverConfig, IMessageHandler messageHandler,
                                 ISessionService sessionService, ServerBootstrap serverBootstrap) {
        this.serverConfig = serverConfig;
        this.messageHandler = messageHandler;
        this.sessionService = sessionService;
        this.serverBootstrap = serverBootstrap;
    }

    @Override
    public void start() {
        for (short port : this.serverConfig.getPorts()) {
            try {
                this.serverBootstrap.bind(new InetSocketAddress(this.serverConfig.getHost(), port)).addListener(objectFuture -> {
                    if (!objectFuture.isSuccess()) {
                        log.error("Failed to start sockets on " + this.serverConfig.getHost() + ", port: " + port);
                    }
                });

                log.info("CometServer listening on port: " + port);
            } catch (Exception e) {
                log.error("Failed to start sockets on " + this.serverConfig.getHost() + ", port: " + port, e);
            }
        }
    }

    @Override
    public NetworkingServerConfig getServerConfig() {
        return serverConfig;
    }

    @Override
    public IMessageHandler getMessageHandler() {
        return messageHandler;
    }

    @Override
    public ISessionService getSessionService() {
        return sessionService;
    }
}
