package com.cometproject.server.network.messages.incoming.quests;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class OpenQuestsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
//        client.send(new QuestListMessageComposer(QuestManager.getInstance().getQuests(), client.getPlayer()));
    }
}
