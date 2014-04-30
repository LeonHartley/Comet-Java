package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.RoomCategoriesMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class LoadCategoriesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        // Moved this to the login event
        //client.send(RoomCategoriesMessageComposer.compose(GameEngine.getNavigator().getCategories(), client.getPlayer().getData().getRank()));
    }
}
