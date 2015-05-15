package com.cometproject.server.network.messages.incoming.quests;

import com.cometproject.server.game.quests.Quest;
import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.network.messages.outgoing.quests.QuestListMessageComposer;
import com.cometproject.server.network.messages.outgoing.quests.QuestStartedMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class StartQuestMessageEvent implements com.cometproject.server.network.messages.incoming.Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int questId = msg.readInt();

        if (client.getPlayer().getQuests().hasStartedQuest(questId)) {
            // Already started it!
            return;
        }

        if (client.getPlayer().getData().getQuestId() != 0) {
            // We need to cancel their current one.
            client.getPlayer().getQuests().cancelQuest(client.getPlayer().getData().getQuestId());
        }

        final Quest quest = QuestManager.getInstance().getById(questId);

        if (quest == null) {
            return;
        }

        client.getPlayer().getData().setQuestId(questId);
        client.getPlayer().getQuests().startQuest(quest);

        client.send(new QuestStartedMessageComposer(quest, client.getPlayer()));
        client.send(new QuestListMessageComposer(QuestManager.getInstance().getQuests(), client.getPlayer(), false));
    }
}
