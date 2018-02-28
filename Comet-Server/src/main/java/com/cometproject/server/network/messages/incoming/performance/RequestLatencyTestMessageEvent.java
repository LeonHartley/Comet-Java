package com.cometproject.server.network.messages.incoming.performance;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.misc.PingMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class RequestLatencyTestMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.setLastPing(Comet.getTime());
        client.send(new PingMessageComposer());
    }
}
