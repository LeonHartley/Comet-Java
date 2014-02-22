package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PopularRoomsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(NavigatorFlatListMessageComposer.compose(0, 2, "", GameEngine.getRooms().getActiveRooms()));
    }
}
