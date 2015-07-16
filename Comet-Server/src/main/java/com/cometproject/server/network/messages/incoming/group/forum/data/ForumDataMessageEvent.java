package com.cometproject.server.network.messages.incoming.group.forum.data;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumDataMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class ForumDataMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        Group group = GroupManager.getInstance().get(groupId);

        if(GroupManager.getInstance().get(groupId) != null && client.getPlayer().getGroups().contains(groupId) && group.getData().hasForum()) {
            client.send(new GroupForumDataMessageComposer(group, client.getPlayer().getId()));
        }
    }
}
