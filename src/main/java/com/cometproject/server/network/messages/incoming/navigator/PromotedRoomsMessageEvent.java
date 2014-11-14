package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;

public class PromotedRoomsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {

        // Get all the promoted rooms.

        client.send(NavigatorFlatListMessageComposer.compose(-1, 0, "", new ArrayList<>()));
    }
}
