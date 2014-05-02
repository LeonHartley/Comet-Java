package com.cometproject.server.game.players.components;

import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.storage.queries.bots.PlayerBotDao;

import java.util.Map;

public class BotComponent {
    private Player player;
    private Map<Integer, InventoryBot> bots;

    public BotComponent(Player player) {
        this.player = player;

        this.bots = PlayerBotDao.getBotsByPlayerId(player.getId());
    }

    public void addBot(InventoryBot bot) {
        this.bots.put(bot.getId(), bot);
    }

    public void dispose() {
        this.bots.clear();
        this.bots = null;
        this.player = null;
    }

    public InventoryBot getBot(int id) {
        if (this.bots.containsKey(id)) {
            return this.bots.get(id);
        }

        return null;
    }

    public void remove(int id) {
        this.bots.remove(id);
    }

    public boolean isBot(int id) {
        return this.bots.containsKey(id);
    }

    public Map<Integer, InventoryBot> getBots() {
        return this.bots;
    }

    public Player getPlayer() {
        return this.player;
    }
}
