package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.players.data.components.bots.IInventoryBot;

import java.util.Map;

public interface IInventoryBotComponent {
    void remove(int id);

    boolean isBot(int id);

    Map<Integer, IInventoryBot> getBots();

    IPlayer getPlayer();

    void clearBots();
}
