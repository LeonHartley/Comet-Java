package com.cometproject.server.network.messages.incoming.group.forum.threads;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumThreadsMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class ForumThreadsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        int start = msg.readInt();
        int end = msg.readInt();

        client.send(new GroupForumThreadsMessageComposer(groupId));
    }
}
