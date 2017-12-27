package com.cometproject.game.landing;

import com.cometproject.api.config.ModuleConfig;
import com.cometproject.api.game.GameContext;
import com.cometproject.api.modules.BaseModule;
import com.cometproject.api.server.IGameService;
import com.cometproject.game.landing.services.LandingService;
import com.cometproject.storage.api.StorageContext;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public class LandingsModule extends BaseModule {

    private LandingService landingService;

    public LandingsModule(ModuleConfig config, IGameService gameService) {
        super(config, gameService);
    }

    @Override
    public void setup() {
        this.landingService = new LandingService(this.getGameService().getExecutorService(), StorageContext.getCurrentContext().getHallOfFameRepository(), StorageContext.getCurrentContext().getPromoArticleRepository());
    }

    @Override
    public void initialiseServices(GameContext gameContext) {
        gameContext.setLandingService(this.landingService);
    }
}
