package com.cometproject.server.network.messages.incoming.room.bots;

import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.bots.RoomBotDao;

public class PlaceBotMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int botId = msg.readInt();
        int x = msg.readInt();
        int y = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();
        InventoryBot bot = client.getPlayer().getBots().getBot(botId);

        if (room == null || bot == null || (!room.getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        double height = room.getModel().getSquareHeight()[x][y];

        if (room.getEntities().getEntitiesAt(x, y).size() >= 1 || !room.getMapping().isValidPosition(new Position(x, y, height))) {
            return;
        }

        RoomBotDao.savePosition(x, y, height, botId, room.getId());

        BotEntity botEntity = room.getBots().addBot(bot, x, y);
        client.getPlayer().getBots().remove(botId);

        room.getEntities().broadcastMessage(AvatarsMessageComposer.compose(botEntity));
        client.send(BotInventoryMessageComposer.compose(client.getPlayer().getBots().getBots()));
    }
}
