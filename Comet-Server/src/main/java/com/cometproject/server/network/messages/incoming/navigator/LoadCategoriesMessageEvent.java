package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.EventCategoriesMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class LoadCategoriesMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
//        client.send(new RoomCategoriesMessageComposer(NavigatorManager.getInstance().getCategories(), client.getPlayer().getData().getRank()));
        client.send(new EventCategoriesMessageComposer());
    }
}
