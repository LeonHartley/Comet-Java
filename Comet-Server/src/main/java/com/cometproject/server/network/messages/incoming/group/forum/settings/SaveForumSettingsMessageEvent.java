package com.cometproject.server.network.messages.incoming.group.forum.settings;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.server.composers.group.GroupInformationMessageComposer;
import com.cometproject.server.composers.group.forums.GroupForumDataMessageComposer;
import com.cometproject.server.composers.group.forums.GroupForumThreadsMessageComposer;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
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

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null || group.getData().getOwnerId() != client.getPlayer().getId()) {
            return;
        }

        if (!group.getData().hasForum()) {
            return;
        }

        IForumSettings forumSettings = group.getForum().getForumSettings();

        forumSettings.setReadPermission(whoCanReadForum);
        forumSettings.setModeratePermission(whoCanModerate);
        forumSettings.setStartThreadsPermission(whoCanStartThreads);
        forumSettings.setPostPermission(whoCanPostMessages);

        GameContext.getCurrent().getGroupService().saveForumSettings(forumSettings);

        client.send(new GroupInformationMessageComposer(group, GameContext.getCurrent().getRoomService().getRoomData(group.getData().getRoomId()), false,
                client.getPlayer().getId() == group.getData().getOwnerId(), group.getMembers().getAdministrators().contains(client.getPlayer().getId()),
                group.getMembers().getAll().containsKey(client.getPlayer().getId()) ? 1 : group.getMembers().getMembershipRequests().contains(client.getPlayer().getId()) ? 2 : 0));

        client.send(new NotificationMessageComposer("forums.forum_settings_updated"));
        client.send(new GroupForumDataMessageComposer(group, client.getPlayer().getId()));

        // HACK, WHEN THIS IS FIXED, REMOVE!
        client.send(new GroupForumThreadsMessageComposer(group.getId(), group.getForum().getForumThreads(0), 0));
    }
}
