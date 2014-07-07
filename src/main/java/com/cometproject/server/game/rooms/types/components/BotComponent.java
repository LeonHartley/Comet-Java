package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.data.PlayerBotData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.storage.queries.bots.RoomBotDao;

import java.util.List;

public class BotComponent {
    private Room room;

    public BotComponent(Room room) {
        this.room = room;

        this.load();
    }

    public void load() {
        try {
            List<BotData> botData = RoomBotDao.getBotsByRoomId(this.room.getId());

            for (BotData data : botData) {
                BotEntity botEntity = new BotEntity(data, room.getEntities().getFreeId(), ((PlayerBotData) data).getPosition(), 2, 2, room);

                this.getRoom().getEntities().addEntity(botEntity);
            }

        } catch (Exception e) {
            room.log.error("Error while deploying bots", e);
        }
    }

    public BotEntity addBot(InventoryBot bot, int x, int y) {
        int virtualId = room.getEntities().getFreeId();

        BotData botData = new PlayerBotData(bot.getId(), bot.getName(), bot.getMotto(), bot.getFigure(), bot.getGender(), bot.getOwnerName(), bot.getOwnerId(), "[]", true, 7);
        BotEntity botEntity = new BotEntity(botData, virtualId, new Position3D(x, y, 0), 1, 1, room);

        this.getRoom().getEntities().addEntity(botEntity);
        return botEntity;
    }

    public Room getRoom() {
        return this.room;
    }
}
