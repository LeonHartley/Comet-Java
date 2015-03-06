package com.cometproject.server.network.messages.incoming.quests;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class OpenQuestsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
//        client.send(new QuestListMessageComposer(QuestManager.getInstance().getQuests(), client.getPlayer()));
    }
}
