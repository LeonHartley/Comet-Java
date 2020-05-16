package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.bots.IBotData;

import java.util.Map;

public interface PlayerBots {
    void remove(int id);

    boolean isBot(int id);

    IBotData getBot(int id);

    void addBot(IBotData bot);

    Map<Integer, IBotData> getBots();

    void clearBots();
}
