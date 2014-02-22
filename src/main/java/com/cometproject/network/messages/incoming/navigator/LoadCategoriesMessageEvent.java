package com.cometproject.network.messages.incoming.navigator;

import com.cometproject.game.GameEngine;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.navigator.RoomCategoriesMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class LoadCategoriesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(RoomCategoriesMessageComposer.compose(GameEngine.getNavigator().getCategories(), client.getPlayer().getData().getRank()));
    }
}
