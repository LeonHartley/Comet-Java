package com.cometproject.network.messages.incoming.navigator;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class OwnRoomsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(NavigatorFlatListMessageComposer.compose(0, 5, "", client.getPlayer().getRooms().values()));
    }
}
