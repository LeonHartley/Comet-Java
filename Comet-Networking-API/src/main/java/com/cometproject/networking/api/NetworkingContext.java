package com.cometproject.networking.api;

public class NetworkingContext {
    private static NetworkingContext currentContext;

    private final INetworkingServerFactory serverFactory;

    public NetworkingContext(INetworkingServerFactory serverFactory) {
        this.serverFactory = serverFactory;
    }

    public INetworkingServerFactory getServerFactory() {
        return serverFactory;
    }

    public static NetworkingContext getCurrentContext() {
        return currentContext;
    }

    public static void setCurrentContext(NetworkingContext networkingContext) {
        currentContext = networkingContext;
    }
}
