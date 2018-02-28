package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class UniqueIdMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String deviceId = msg.readString();
        String fingerprint = msg.readString();
        if (fingerprint == null) {
            return;
        } // Mandoe & Ryan fix

//        if(deviceId == null)
//            deviceId = fingerprint;

        client.setUniqueId(fingerprint);
    }
}
