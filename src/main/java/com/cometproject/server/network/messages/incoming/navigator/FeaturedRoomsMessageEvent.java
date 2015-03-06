package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.FeaturedRoomsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class FeaturedRoomsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        client.send(new FeaturedRoomsMessageComposer(NavigatorManager.getInstance().getFeaturedRooms()));
    }
}
