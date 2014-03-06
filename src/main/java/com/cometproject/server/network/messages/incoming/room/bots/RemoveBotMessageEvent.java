package com.cometproject.server.network.messages.incoming.room.bots;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class RemoveBotMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        BotEntity entity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByBotId(msg.readInt());

        if(entity == null) {
            return;
        }

        InventoryBot bot = new InventoryBot(entity.getBotId(), entity.getData().getOwnerId(), entity.getData().getOwnerName(), entity.getUsername(), entity.getFigure(), entity.getGender(), entity.getMotto());
        client.getPlayer().getBots().addBot(bot);

        entity.leaveRoom();
        client.send(BotInventoryMessageComposer.compose(client.getPlayer().getBots().getBots()));

        Comet.getServer().getStorage().execute("UPDATE bots SET room_id = 0 WHERE id = " + bot.getId());
    }
}
