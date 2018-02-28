package com.cometproject.server.network.messages.incoming.group.forum.data;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.server.composers.group.forums.GroupForumListMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Lists;

import java.util.List;

public class GetForumsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        List<IGroup> groups = Lists.newArrayList();

        for (int groupId : client.getPlayer().getGroups()) {
            final IGroupData groupData = GameContext.getCurrent().getGroupService().getData(groupId);

            if (groupData != null && groupData.hasForum()) {
                final IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

                if (group.getForum() != null)
                    groups.add(group);
            }
        }

        client.send(new GroupForumListMessageComposer(msg.readInt(), groups, client.getPlayer().getId()));
    }
}
