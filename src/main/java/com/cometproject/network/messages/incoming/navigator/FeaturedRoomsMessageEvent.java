package com.cometproject.network.messages.incoming.navigator;

import com.cometproject.game.GameEngine;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.navigator.FeaturedRoomsMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class FeaturedRoomsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        client.send(FeaturedRoomsMessageComposer.compose(GameEngine.getNavigator().getFeaturedRooms()));
    }
}
