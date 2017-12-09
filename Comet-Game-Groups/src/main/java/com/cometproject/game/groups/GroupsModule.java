package com.cometproject.game.groups;

import com.cometproject.api.config.ModuleConfig;
import com.cometproject.api.game.GameContext;
import com.cometproject.api.modules.BaseModule;
import com.cometproject.api.server.IGameService;

public class GroupsModule extends BaseModule {
    public GroupsModule(ModuleConfig config, IGameService gameService) {
        super(config, gameService);
    }

}
