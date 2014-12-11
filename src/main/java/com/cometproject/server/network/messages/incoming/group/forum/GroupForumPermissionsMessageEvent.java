package com.cometproject.server.network.messages.incoming.group.forum;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumPermissionsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class GroupForumPermissionsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null) {
            return;
        }

        client.send(GroupForumPermissionsMessageComposer.compose(groupId));
    }
}
