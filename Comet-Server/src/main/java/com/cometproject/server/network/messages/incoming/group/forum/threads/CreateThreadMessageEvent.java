package com.cometproject.server.network.messages.incoming.group.forum.threads;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class CreateThreadMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int groupId = msg.readInt();
        final int threadId = msg.readInt();
        final String subject = msg.readString();
        final String message = msg.readString();

        if(threadId == 0) {
            // New thread!
        } else {
            // Reply to a thread!
        }

    }
}
