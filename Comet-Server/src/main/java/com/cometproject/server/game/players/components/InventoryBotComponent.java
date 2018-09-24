package com.cometproject.server.game.players.components;

import com.cometproject.api.game.bots.IBotData;
import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.players.data.components.PlayerBots;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.storage.queries.bots.PlayerBotDao;

import java.util.Map;


public class InventoryBotComponent extends PlayerComponent implements PlayerBots {
    private Map<Integer, IBotData> bots;

    public InventoryBotComponent(IPlayer player) {
        super(player);

        this.bots = PlayerBotDao.getBotsByPlayerId(player.getId());
    }

    @Override
    public void addBot(IBotData bot) {
        this.bots.put(bot.getId(), bot);

        this.getPlayer().flush();
    }

    @Override
    public IBotData getBot(int id) {
        if (this.bots.containsKey(id)) {
            return this.bots.get(id);
        }

        return null;
    }

    @Override
    public void remove(int id) {
        this.bots.remove(id);

        this.getPlayer().flush();
    }

    @Override
    public boolean isBot(int id) {
        return this.bots.containsKey(id);
    }

    @Override
    public Map<Integer, IBotData> getBots() {
        return this.bots;
    }

    @Override
    public void clearBots() {
        this.bots.clear();

        this.getPlayer().flush();
    }

    @Override
    public void dispose() {
        super.dispose();

        this.bots.clear();
        this.bots = null;
    }
}
