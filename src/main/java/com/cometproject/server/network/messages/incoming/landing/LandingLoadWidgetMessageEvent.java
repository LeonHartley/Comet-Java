package com.cometproject.server.network.messages.incoming.landing;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.landing.HotelViewItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class LandingLoadWidgetMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        final String text = msg.readString();
        final String[] splitText = text.split(",");

        if (text.isEmpty() || splitText.length < 2) {
            client.sendQueue(new HotelViewItemMessageComposer("", ""));
            return;
        }

        if (splitText[1].equals("gamesmaker")) return;

        client.sendQueue(new HotelViewItemMessageComposer(text, splitText[1]));
    }
}
