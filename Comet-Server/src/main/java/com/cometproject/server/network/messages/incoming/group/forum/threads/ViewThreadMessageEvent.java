package com.cometproject.server.network.messages.incoming.group.forum.threads;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.server.composers.group.forums.GroupForumViewThreadMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class ViewThreadMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int threadId = msg.readInt();
        int indexStart = msg.readInt();

        final IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null) {
            return;
        }

        IForumThread forumThread = group.getForum().getForumThreads().get(threadId);

        if (forumThread == null) {
            return;
        }

        if (group.getForum().getForumSettings().getReadPermission() == ForumPermission.MEMBERS) {
            if (!group.getMembers().getAll().containsKey(client.getPlayer().getId())) {
                return;
            }
        } else if (group.getForum().getForumSettings().getReadPermission() == ForumPermission.ADMINISTRATORS) {
            if (!group.getMembers().getAdministrators().contains(client.getPlayer().getId())) {
                return;
            }
        }

        if (forumThread.getState() != 1) {
            // TODO: do the shizzle.
            return;
        }

        client.send(new GroupForumViewThreadMessageComposer(group.getData(), threadId,
                forumThread.getReplies(indexStart), indexStart));
    }
}
