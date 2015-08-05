package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.quests.types.QuestType;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class ApplyDanceMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int danceId = msg.readInt();

        if (client.getPlayer().getEntity().getDanceId() == danceId) {
            return;
        }

        client.getPlayer().getEntity().unIdle();

        if(!client.getPlayer().getEntity().isVisible()) {
            return;
        }

        client.getPlayer().getEntity().setDanceId(danceId);
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new DanceMessageComposer(client.getPlayer().getEntity().getId(), danceId));

        client.getPlayer().getQuests().progressQuest(QuestType.SOCIAL_DANCE);
    }
}
