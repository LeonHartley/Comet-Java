package com.cometproject.server.network.messages.incoming.catalog.data;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.data.GiftWrappingConfigurationMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class GetGiftWrappingConfigurationMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(new GiftWrappingConfigurationMessageComposer());
    }
}
