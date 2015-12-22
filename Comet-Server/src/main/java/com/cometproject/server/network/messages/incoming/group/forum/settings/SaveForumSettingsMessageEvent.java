package com.cometproject.server.network.messages.incoming.group.forum.settings;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumPermission;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumThreadsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class SaveForumSettingsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int groupId = msg.readInt();

        int readForum = msg.readInt();
        int postMessages = msg.readInt();
        int startThreads = msg.readInt();
        int moderate = msg.readInt();

        final ForumPermission whoCanReadForum = ForumPermission.getById(readForum);
        final ForumPermission whoCanPostMessages = ForumPermission.getById(postMessages);
        final ForumPermission whoCanStartThreads = ForumPermission.getById(startThreads);
        final ForumPermission whoCanModerate = ForumPermission.getById(moderate);

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null || group.getData().getOwnerId() != client.getPlayer().getId()) {
            return;
        }

        if (!group.getData().hasForum()) {
            return;
        }

        ForumSettings forumSettings = group.getForumComponent().getForumSettings();

        forumSettings.setReadPermission(whoCanReadForum);
        forumSettings.setModeratePermission(whoCanModerate);
        forumSettings.setStartThreadsPermission(whoCanStartThreads);
        forumSettings.setPostPermission(whoCanPostMessages);

        forumSettings.save();

        client.send(group.composeInformation(false, client.getPlayer().getId()));
        client.send(new NotificationMessageComposer("forums.forum_settings_updated"));
        client.send(new GroupForumDataMessageComposer(group, client.getPlayer().getId()));

        // HACK, WHEN THIS IS FIXED, REMOVE!
        client.send(new GroupForumThreadsMessageComposer(group.getId(), group.getForumComponent().getForumThreads(0), 0));
    }
}
