package com.cometproject.server.network.messages.incoming.group.forum.threads;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumPermission;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
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

        final Group group = GroupManager.getInstance().get(groupId);

        if(group == null) {
            return;
        }

        ForumThread forumThread = group.getForumComponent().getForumThreads().get(threadId);

        if(forumThread == null) {
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

        if(forumThread.getState() != 1) {
            // TODO: do the shizzle.
            return;
        }

        client.send(new GroupForumViewThreadMessageComposer(group.getData(), threadId, forumThread.getReplies(indexStart), indexStart));
    }
}
