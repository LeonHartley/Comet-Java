package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.players.data.components.bots.PlayerBot;

import java.util.Map;

public interface PlayerBots {
    void remove(int id);

    boolean isBot(int id);

    Map<Integer, PlayerBot> getBots();

    void clearBots();
}
