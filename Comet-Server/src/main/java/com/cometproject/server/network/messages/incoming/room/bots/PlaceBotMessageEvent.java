package com.cometproject.server.network.messages.incoming.room.bots;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.players.components.types.inventory.InventoryBot;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.bots.RoomBotDao;


public class PlaceBotMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int botId = msg.readInt();
        int x = msg.readInt();
        int y = msg.readInt();

        RoomInstance room = client.getPlayer().getEntity().getRoom();
        InventoryBot bot = client.getPlayer().getBots().getBot(botId);

        if (room == null || bot == null || (!room.getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        if(room.getEntities().getBotEntities().size() >= CometSettings.maxBotsInRoom) {
            client.send(new AlertMessageComposer(String.format(Locale.getOrDefault("comet.game.bots.toomany", "You can only have %s bots per room!"), CometSettings.maxBotsInRoom)));
            return;
        }

        double height = room.getMapping().getTile(x, y).getWalkHeight();
        final Position position = new Position(x, y, height);

        if (room.getEntities().getEntitiesAt(position).size() >= 1 || !room.getMapping().isValidPosition(position)) {
            return;
        }

        RoomBotDao.savePosition(x, y, height, botId, room.getId());

        BotEntity botEntity = room.getBots().addBot(bot, x, y, height);
        client.getPlayer().getBots().remove(botId);

        room.getEntities().broadcastMessage(new AvatarsMessageComposer(botEntity));
        client.send(new BotInventoryMessageComposer(client.getPlayer().getBots().getBots()));

        for(RoomItemFloor floorItem : room.getItems().getItemsOnSquare(x, y)) {
            floorItem.onEntityStepOn(botEntity);
        }
    }
}
