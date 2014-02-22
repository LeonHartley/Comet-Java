package com.cometproject.game.players.components;

import com.cometproject.boot.Comet;
import com.cometproject.game.players.components.types.InventoryBot;
import com.cometproject.game.players.types.Player;
import javolution.util.FastMap;

import java.sql.ResultSet;
import java.util.Map;

public class BotComponent {
    private Player player;
    private Map<Integer, InventoryBot> bots;

    public BotComponent(Player player) {
        this.player = player;
        this.bots = new FastMap<>();

        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM bots WHERE owner_id = " + this.getPlayer().getId() + " AND room_id = 0");

            while(data.next()) {
                this.bots.put(data.getInt("id"), new InventoryBot(data));
            }
        } catch(Exception e) {
            player.getSession().getLogger().error("Error while loading player bots", e);
        }
    }

    public void dispose() {
        this.bots.clear();
        this.bots = null;
        this.player = null;
    }

    public InventoryBot getBot(int id) {
        if(this.bots.containsKey(id)) {
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
