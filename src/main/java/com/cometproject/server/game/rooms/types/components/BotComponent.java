package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.players.components.types.inventory.InventoryBot;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.data.PlayerBotData;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.storage.queries.bots.RoomBotDao;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;


public class BotComponent {
    private Room room;

    private Map<String, Integer> botNameToId;

    public BotComponent(Room room) {
        this.room = room;

        this.botNameToId = Maps.newHashMap();

        this.load();
    }

    public void load() {
        try {
            List<BotData> botData = RoomBotDao.getBotsByRoomId(this.room.getId());

            for (BotData data : botData) {

                if(this.botNameToId.containsKey(data.getUsername())) {
                    data.setUsername(this.getAvailableName(data.getUsername()));
                }

                BotEntity botEntity = new BotEntity(data, room.getEntities().getFreeId(), ((PlayerBotData) data).getPosition(), 2, 2, room);
                this.botNameToId.put(botEntity.getUsername(), botEntity.getBotId());

                this.getRoom().getEntities().addEntity(botEntity);
            }
        } catch (Exception e) {
            room.log.error("Error while deploying bots", e);
        }
    }

    public String getAvailableName(String name) {
        int usedCount = 0;

        for(String usedName : this.botNameToId.keySet()) {
            if(name.startsWith(usedName)) {
                usedCount++;
            }
        }

        if(usedCount == 0) return name;
        
        return name + usedCount;
    }

    public BotEntity addBot(InventoryBot bot, int x, int y) {
        int virtualId = room.getEntities().getFreeId();
        String name;

        if(this.botNameToId.containsKey(bot.getName())) {
            name = this.getAvailableName(bot.getName());
        } else {
            name = bot.getName();
        }

        BotData botData = new PlayerBotData(bot.getId(), name, bot.getMotto(), bot.getFigure(), bot.getGender(), bot.getOwnerName(), bot.getOwnerId(), "[]", true, 7, bot.getType(), bot.getMode());
        BotEntity botEntity = new BotEntity(botData, virtualId, new Position(x, y, 0), 1, 1, room);

        this.getRoom().getEntities().addEntity(botEntity);
        return botEntity;
    }

    public Room getRoom() {
        return this.room;
    }

    public void changeBotName(String currentName, String newName) {
        if(!this.botNameToId.containsKey(currentName)) return;

        int botId = this.botNameToId.get(currentName);

        this.botNameToId.remove(currentName);
        this.botNameToId.put(newName, botId);
    }
}
