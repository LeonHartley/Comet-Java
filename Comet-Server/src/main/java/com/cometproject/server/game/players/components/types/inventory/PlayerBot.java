package com.cometproject.server.game.players.components.types.inventory;

import com.cometproject.api.game.bots.IBotData;
import com.cometproject.api.game.players.data.components.bots.IPlayerBot;


public class PlayerBot implements IPlayerBot {
    private IBotData botData;

    public PlayerBot(IBotData botData) {
        this.botData = botData;
    }

    @Override
    public int getId() {
        return this.botData.getId();
    }

    @Override
    public IBotData getBotData() {
        return this.botData;
    }
}
