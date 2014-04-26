package com.cometproject.server.network.messages.incoming.user.wardrobe;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.wardrobe.WardrobeMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class WardrobeMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.send(WardrobeMessageComposer.compose(client.getPlayer().getSettings().getWardrobe()));
    }
}
