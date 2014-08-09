package com.cometproject.server.network.messages.incoming.room.item.gifts;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class OpenGiftMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.send(AdvancedAlertMessageComposer.compose("Disabled", "Feature disabled."));
    }
}
