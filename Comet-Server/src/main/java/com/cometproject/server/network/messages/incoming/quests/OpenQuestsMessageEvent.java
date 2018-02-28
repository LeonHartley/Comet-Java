package com.cometproject.server.network.messages.incoming.quests;

import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.quests.QuestListMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class OpenQuestsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new QuestListMessageComposer(QuestManager.getInstance().getQuests(), client.getPlayer(), true));
    }
}
