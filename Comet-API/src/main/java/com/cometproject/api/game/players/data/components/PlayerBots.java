package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.players.data.components.bots.IPlayerBot;

import java.util.Map;

public interface PlayerBots {
    void remove(int id);

    boolean isBot(int id);

    IPlayerBot getBot(int id);

    void addBot(IPlayerBot bot);

    Map<Integer, IPlayerBot> getBots();

    void clearBots();
}
