package com.cometproject.server.network.messages.incoming.room.bots;

import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class BotChatListMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int botId = msg.readInt();
        int skillId = msg.readInt();

        BotEntity entity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByBotId(botId);

        if(entity == null) {
            return;
        }

        switch(skillId) {
            case 2:

                break;

            case 5:

                break;
        }
    }
}
