package com.cometproject.game.rooms;

import com.cometproject.api.config.ModuleConfig;
import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.rooms.models.IRoomModelFactory;
import com.cometproject.api.game.rooms.models.IRoomModelService;
import com.cometproject.api.modules.BaseModule;
import com.cometproject.api.server.IGameService;
import com.cometproject.game.rooms.factories.RoomModelFactory;
import com.cometproject.game.rooms.services.RoomModelService;
import com.cometproject.storage.api.StorageContext;

public class RoomsModule extends BaseModule {
    private IRoomModelService roomModelService;

    public RoomsModule(ModuleConfig config, IGameService gameService) {
        super(config, gameService);
    }

    @Override
    public void setup() {
        final IRoomModelFactory roomModelFactory = new RoomModelFactory();

        this.roomModelService = new RoomModelService(roomModelFactory,
                StorageContext.getCurrentContext().getRoomRepository());
    }

    @Override
    public void initialiseServices(GameContext gameContext) {
        this.roomModelService.loadModels();

        gameContext.setRoomModelService(this.roomModelService);
    }
}
