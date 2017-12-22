package com.cometproject.gamecenter.fastfood;

import com.cometproject.api.config.ModuleConfig;
import com.cometproject.api.game.GameContext;
import com.cometproject.api.modules.BaseModule;
import com.cometproject.api.server.IGameService;
import com.cometproject.gamecenter.fastfood.net.SessionFactory;
import com.cometproject.networking.api.INetworkingServer;
import com.cometproject.networking.api.NetworkingContext;
import com.cometproject.networking.api.config.NetworkingServerConfig;
import com.google.common.collect.Sets;

public class FastFoodModule extends BaseModule {

    private final short serverPort = 30010;
    private INetworkingServer fastFoodServer;

    public FastFoodModule(ModuleConfig config, IGameService gameService) {
        super(config, gameService);
    }

    @Override
    public void setup() {
        this.fastFoodServer = NetworkingContext.getCurrentContext().getServerFactory().createServer(
                new NetworkingServerConfig("0.0.0.0", Sets.newHashSet(serverPort)), new SessionFactory(((messageEvent, session) -> {
                    System.out.println("Received message with id: " + messageEvent.getId());
                })));
    }

    @Override
    public void initialiseServices(GameContext gameContext) {
        this.fastFoodServer.start();
    }
}
