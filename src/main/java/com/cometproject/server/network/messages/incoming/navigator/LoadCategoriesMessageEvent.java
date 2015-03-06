package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.EventCategoriesMessageComposer;
import com.cometproject.server.network.messages.outgoing.navigator.RoomCategoriesMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class LoadCategoriesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(new RoomCategoriesMessageComposer(NavigatorManager.getInstance().getCategories(), client.getPlayer().getData().getRank()));
        client.send(new EventCategoriesMessageComposer());
    }
}
