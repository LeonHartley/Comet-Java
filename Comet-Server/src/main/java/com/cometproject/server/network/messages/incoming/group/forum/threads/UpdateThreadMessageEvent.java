package com.cometproject.server.network.messages.incoming.group.forum.threads;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumPermission;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumUpdateThreadMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.RoomNotificationMessageComposer;
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

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null || !group.getData().hasForum()) {
            return;
        }

        ForumSettings forumSettings = group.getForumComponent().getForumSettings();

        if (forumSettings.getModeratePermission() == ForumPermission.OWNER) {
            if (client.getPlayer().getId() != group.getData().getId()) {
                return;
            }
        } else {
            if (!group.getMembershipComponent().getAdministrators().contains(client.getPlayer().getId())) {
                return;
            }
        }

        ForumThread forumThread = group.getForumComponent().getForumThreads().get(threadId);

        if (forumThread == null) {
            return;
        }

        if (isPinned != forumThread.isPinned()) {
            GroupForumThreadDao.saveMessagePinnedState(forumThread.getId(), isPinned);
            client.send(new RoomNotificationMessageComposer("forums.thread." + (isPinned ? "pinned" : "unpinned")));

            if (isPinned) {
                group.getForumComponent().getPinnedThreads().add(forumThread.getId());
            } else {
                group.getForumComponent().getPinnedThreads().remove((Integer) forumThread.getId());
            }
        }

        if (isLocked != forumThread.isLocked()) {
            GroupForumThreadDao.saveMessageLockState(forumThread.getId(), isLocked);

            client.send(new RoomNotificationMessageComposer("forums.thread." + (isLocked ? "locked" : "unlocked")));
        }

        forumThread.setIsPinned(isPinned);
        forumThread.setIsLocked(isLocked);

        client.send(new GroupForumUpdateThreadMessageComposer(groupId, forumThread));
    }
}
