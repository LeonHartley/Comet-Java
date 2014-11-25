package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class UniqueIdMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        String deviceId = msg.readString();
        String fingerprint = msg.readString();

//        if(deviceId == null)
//            deviceId = fingerprint;

        client.setUniqueId(fingerprint);
    }
}
