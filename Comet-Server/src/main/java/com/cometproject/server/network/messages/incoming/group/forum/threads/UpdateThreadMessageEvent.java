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

public class UpdateThreadMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int groupId = msg.readInt();
        final int threadId = msg.readInt();
        final boolean isPinned = msg.readBoolean();
        final boolean isLocked = msg.readBoolean();

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

        if (isPinned != forumThread.isPinned()) {
            GroupForumThreadDao.saveMessagePinnedState(forumThread.getId(), isPinned);
            client.send(new NotificationMessageComposer("forums.thread." + (isPinned ? "pinned" : "unpinned")));

            if (isPinned) {
                group.getForum().getPinnedThreads().add(forumThread.getId());
            } else {
                group.getForum().getPinnedThreads().remove((Integer) forumThread.getId());
            }
        }

        if (isLocked != forumThread.isLocked()) {
            GroupForumThreadDao.saveMessageLockState(forumThread.getId(), isLocked, client.getPlayer().getId(), client.getPlayer().getData().getUsername());

            client.send(new NotificationMessageComposer("forums.thread." + (isLocked ? "locked" : "unlocked")));
        }

        forumThread.setIsPinned(isPinned);
        forumThread.setIsLocked(isLocked);

        client.send(new GroupForumUpdateThreadMessageComposer(groupId, forumThread));
    }
}
