package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.bots.Bot;
import com.cometproject.server.game.rooms.types.components.bots.player.PlayerBot;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class BotComponent {
    private Room room;
    private Map<Integer, Bot> bots;
    private int virtualIdCounter = 0;
    private Logger log;

    public BotComponent(Room room) {
        this.room = room;
        this.bots = new FastMap<>();
        this.load();
    }

    public void tick() {
        synchronized (bots) {
            for(Map.Entry<Integer, Bot> botKV : bots.entrySet()) {
                int botId = botKV.getKey();
                Bot bot = botKV.getValue();

                if(!bot.needsRemove()) {
                    bot.tick();
                } else {
                    this.bots.remove(botId);
                    this.log.debug("Bot removed from room (Bot ID: " + botId + ")");
                }
            }
        }
    }

    public void load() {
        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM bots WHERE room_id = " + this.room.getId());

            while(data.next()) {
                virtualIdCounter--;
                this.bots.put(data.getInt("id"), new PlayerBot(virtualIdCounter, data, this.getRoom()));
            }
        } catch(Exception e) {
            room.log.error("Error while deploying bots", e);
        }
    }

    public void addBot(InventoryBot bot, int x, int y) {
        virtualIdCounter--;
        this.bots.put(bot.getId(), new PlayerBot(virtualIdCounter, bot, x, y, this.getRoom()));
    }

    public Bot getBot(int id) {
        return this.bots.get(id);
    }

    public void dispose() {
        this.bots.clear();
        this.bots = null;

        this.room = null;
    }

    public Room getRoom() {
        return this.room;
    }
}
