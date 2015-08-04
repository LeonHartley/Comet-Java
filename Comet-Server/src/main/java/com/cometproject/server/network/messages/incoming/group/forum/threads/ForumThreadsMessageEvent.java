package com.cometproject.server.network.messages.incoming.group.forum.threads;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumPermission;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumThreadsMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class ForumThreadsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        int start = msg.readInt();

        Group group = GroupManager.getInstance().get(groupId);

        if(group == null) {
            return;
        }

        if(group.getForumComponent().getForumSettings().getReadPermission() == ForumPermission.MEMBERS) {
            if(!group.getMembershipComponent().getMembers().containsKey(client.getPlayer().getId())) {
                return;
            }
        } else if(group.getForumComponent().getForumSettings().getReadPermission() == ForumPermission.ADMINISTRATORS) {
            if(!group.getMembershipComponent().getAdministrators().contains(client.getPlayer().getId())) {
                return;
            }
        }

        client.send(new GroupForumThreadsMessageComposer(group.getId(), group.getForumComponent().getForumThreads(start), start));
    }
}
