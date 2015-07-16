package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class UniqueIdMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String deviceId = msg.readString();
        String fingerprint = msg.readString();

//        if(deviceId == null)
//            deviceId = fingerprint;

        client.setUniqueId(fingerprint);
    }
}
