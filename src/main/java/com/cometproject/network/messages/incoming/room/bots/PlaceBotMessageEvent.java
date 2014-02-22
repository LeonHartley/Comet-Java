package com.cometproject.network.messages.incoming.room.bots;

import com.cometproject.boot.Comet;
import com.cometproject.game.players.components.types.InventoryBot;
import com.cometproject.game.rooms.types.Room;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometproject.network.messages.outgoing.room.bots.PlaceBotMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class PlaceBotMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int botId = msg.readInt();
        int x = msg.readInt();
        int y = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();
        InventoryBot bot = client.getPlayer().getBots().getBot(botId);

        if(room == null || bot == null) {
            return;
        }

        /*if(!room.getMapping().isValidPosition(x, y)) {
            return;
        }*/

        // TODO: Check square!

        Comet.getServer().getStorage().execute("UPDATE bots SET room_id = " + room.getId() + ", x = " + x + ", y = " + y + ", z = '0.0' WHERE id = " + botId);
        room.getBots().addBot(bot, x, y);
        client.getPlayer().getBots().remove(botId);

        room.getEntities().broadcastMessage(PlaceBotMessageComposer.compose(room.getBots().getBot(botId)));
        room.getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(room.getBots().getBot(botId)));
    }
}
