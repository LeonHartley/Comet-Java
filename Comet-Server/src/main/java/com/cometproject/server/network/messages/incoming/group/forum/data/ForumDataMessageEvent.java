package com.cometproject.server.network.messages.incoming.group.forum.data;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.server.composers.group.forums.GroupForumDataMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class ForumDataMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null || group.getData() == null || !group.getData().hasForum()) {
            return;
        }

        if (group.getForum() == null) {
            return;
        }

        if (group.getForum().getForumSettings().getReadPermission() == ForumPermission.MEMBERS) {
            if (!group.getMembers().getAll().containsKey(client.getPlayer().getId())) {
                client.send(new NotificationMessageComposer("forums.error.access_denied"));
                return;
            }
        } else if (group.getForum().getForumSettings().getReadPermission() == ForumPermission.ADMINISTRATORS) {
            if (!group.getMembers().getAdministrators().contains(client.getPlayer().getId())) {
                client.send(new NotificationMessageComposer("forums.error.access_denied"));
                return;
            }
        }

        client.send(new GroupForumDataMessageComposer(group, client.getPlayer().getId()));
    }
}
