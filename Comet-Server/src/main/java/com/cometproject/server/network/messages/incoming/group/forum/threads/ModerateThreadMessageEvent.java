package com.cometproject.server.network.messages.incoming.group.forum.threads;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.server.composers.group.forums.GroupForumUpdateThreadMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.groups.GroupForumThreadDao;

public class ModerateThreadMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int groupId = msg.readInt();
        final int threadId = msg.readInt();
        final int state = msg.readInt();

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null || !group.getData().hasForum()) {
            return;
        }

        IForumSettings forumSettings = group.getForum().getForumSettings();

        if (forumSettings.getModeratePermission() == ForumPermission.OWNER) {
            if (client.getPlayer().getId() != group.getData().getId()) {
                return;
            }
        } else {
            if (!group.getMembers().getAdministrators().contains(client.getPlayer().getId())) {
                return;
            }
        }

        IForumThread forumThread = group.getForum().getForumThreads().get(threadId);

        if (forumThread == null) {
            return;
        }

        forumThread.setState(state);
        GroupForumThreadDao.saveMessageState(forumThread.getId(), state, client.getPlayer().getId(), client.getPlayer().getData().getUsername());

        client.send(new NotificationMessageComposer(state == 20 ? "forums.thread.hidden" : "forums.thread.restored"));
        client.send(new GroupForumUpdateThreadMessageComposer(groupId, forumThread));
    }
}
