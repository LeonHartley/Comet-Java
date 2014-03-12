package com.cometproject.server.network.messages.incoming.catalog.marketplace;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.marketplace.MarketplaceConfigurationMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class MarketplaceConfigurationMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.send(MarketplaceConfigurationMessageComposer.compose());
    }
}
