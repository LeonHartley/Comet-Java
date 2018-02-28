package com.cometproject.server.network.messages.incoming.quests;

import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.quests.QuestListMessageComposer;
import com.cometproject.server.network.messages.outgoing.quests.QuestStoppedMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class CancelQuestMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int questId = client.getPlayer().getData().getQuestId();

        if (questId != 0) {
            client.getPlayer().getQuests().cancelQuest(questId);
            client.getPlayer().getData().setQuestId(0);

            client.getPlayer().getData().save();
        }

        client.send(new QuestStoppedMessageComposer());
        client.send(new QuestListMessageComposer(QuestManager.getInstance().getQuests(), client.getPlayer(), false));
    }
}
