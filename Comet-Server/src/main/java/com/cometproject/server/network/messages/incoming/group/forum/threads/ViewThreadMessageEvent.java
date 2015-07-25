package com.cometproject.server.network.messages.incoming.group.forum.threads;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumViewThreadMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class ViewThreadMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int threadId = msg.readInt();
        int indexStart = msg.readInt();
        int indexFinish = msg.readInt();

        final Group group = GroupManager.getInstance().get(groupId);

        if(group == null) {
            return;
        }

        // TODO: more checks.

        client.send(new GroupForumViewThreadMessageComposer(group.getData(), group.getForumComponent().getForumThreads().get(threadId), indexStart, indexFinish));
    }
}
