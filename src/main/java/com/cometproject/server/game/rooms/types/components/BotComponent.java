package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.data.PlayerBotData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.bots.Bot;
import com.cometproject.server.game.rooms.types.components.bots.player.PlayerBot;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class BotComponent {
    private Room room;

    private Map<Integer, BotData> botDataInstances;

    public BotComponent(Room room) {
        this.room = room;
        this.botDataInstances = new FastMap<>();

        this.load();
    }

    public void load() {
        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM bots WHERE room_id = " + this.room.getId());

            while(data.next()) {
                BotData botData = new PlayerBotData(data.getInt("id"), data.getString("name"), data.getString("motto"), data.getString("figure"), data.getString("gender"), data.getString("owner"), data.getInt("owner_id"), data.getString("messages"), data.getString("automatic_chat").equals("1"), data.getInt("chat_delay"));
                BotEntity botEntity = new BotEntity(botData, room.getEntities().getFreeId(), new Position3D(data.getInt("x"), data.getInt("y"), data.getInt("z")), 1, 1, room);

                this.botDataInstances.put(botData.getId(), botData);
                this.getRoom().getEntities().addEntity(botEntity);
            }
        } catch(Exception e) {
            room.log.error("Error while deploying bots", e);
        }
    }

    public BotData getBotData(int id) {
        return this.botDataInstances.get(id);
    }

    public BotEntity addBot(InventoryBot bot, int x, int y) {
        int virtualId = room.getEntities().getFreeId();

        BotData botData = new PlayerBotData(bot.getId(), bot.getName(), bot.getMotto(), bot.getFigure(), bot.getGender(), bot.getOwnerName(), bot.getOwnerId(), "[]", true, 7);
        BotEntity botEntity = new BotEntity(botData, virtualId, new Position3D(x, y, 0), 1, 1, room);

        this.botDataInstances.put(botData.getId(), botData);
        this.getRoom().getEntities().addEntity(botEntity);
        return botEntity;
    }

    public void dispose() {
        this.room = null;
    }

    public Room getRoom() {
        return this.room;
    }
}
