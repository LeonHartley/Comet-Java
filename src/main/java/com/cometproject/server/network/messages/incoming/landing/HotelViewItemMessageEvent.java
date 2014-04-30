package com.cometproject.server.network.messages.incoming.landing;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.landing.HotelViewItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class HotelViewItemMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        String[] data = msg.readString().split(":");

        for(int i = 0; i < data.length; i++) {
            if(data[i].contains(",")) {
                client.send(HotelViewItemMessageComposer.compose(data[i], data[i].split(",")[1]));
            }
        }
    }
}
