package com.cometproject.server.network.messages.incoming.landing;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.landing.HotelViewItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class LandingLoadWidgetMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        String text = msg.readString();

        if (text.isEmpty()) {
            client.sendQueue(HotelViewItemMessageComposer.compose("", ""));
            return;
        }

        if (text.split(",")[1].equals("gamesmaker")) return;

        client.sendQueue(HotelViewItemMessageComposer.compose(text, text.split(",")[1]));
    }
}
