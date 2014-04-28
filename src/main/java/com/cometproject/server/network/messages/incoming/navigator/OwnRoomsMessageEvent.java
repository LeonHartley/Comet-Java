package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;

public class OwnRoomsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(NavigatorFlatListMessageComposer.compose(0, 5, "", new ArrayList<>(client.getPlayer().getRooms().values()), false));
    }
}
