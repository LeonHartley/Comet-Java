package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.PopularTagsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class LoadSearchRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(PopularTagsMessageComposer.compose());
    }
}
