package com.cometsrv.network.messages.incoming.room.bots;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.players.components.types.InventoryBot;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometsrv.network.messages.outgoing.room.bots.PlaceBotMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class PlaceBotMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int botId = msg.readInt();
        int x = msg.readInt();
        int y = msg.readInt();

        Room room = client.getPlayer().getAvatar().getRoom();
        InventoryBot bot = client.getPlayer().getBots().getBot(botId);
        if(room == null || bot == null) return;

        /*if(client.getPlayer().getEntity().getPathfinder() == null) {
            client.getPlayer().getEntity().setPathfinder();
        }*/

        if(!client.getPlayer().getEntity().getPathfinder().canWalk(x, y)) {
            return;
        }

        Comet.getServer().getStorage().execute("UPDATE bots SET room_id = " + room.getId() + ", x = " + x + ", y = " + y + ", z = '0.0' WHERE id = " + botId);
        room.getBots().addBot(bot, x, y);
        client.getPlayer().getBots().remove(botId);

        room.getEntities().broadcastMessage(PlaceBotMessageComposer.compose(room.getBots().getBot(botId)));
        room.getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(room.getBots().getBot(botId)));
    }
}
