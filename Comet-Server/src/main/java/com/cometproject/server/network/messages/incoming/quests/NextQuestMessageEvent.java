package com.cometproject.server.network.messages.incoming.quests;

import com.cometproject.api.game.quests.IQuest;
import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class NextQuestMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer().getData().getQuestId() == 0) {
            return;
        }

        IQuest quest = QuestManager.getInstance().getById(client.getPlayer().getData().getQuestId());

        if (quest == null) {
            return;
        }

        if (!client.getPlayer().getQuests().hasCompletedQuest(quest.getId())) {
            return;
        }

        IQuest nextQuest = QuestManager.getInstance().getNextQuestInSeries(quest);

        if (nextQuest != null) {
            client.getPlayer().getQuests().startQuest(nextQuest);
        }
    }
}
