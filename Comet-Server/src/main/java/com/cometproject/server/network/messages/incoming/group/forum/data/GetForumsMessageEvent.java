package com.cometproject.server.network.messages.incoming.group.forum.data;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumListMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Lists;

import java.util.List;

public class GetForumsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        List<Group> myGroups = Lists.newArrayList();

        for(int groupId : client.getPlayer().getGroups()) {
            final GroupData groupData = GroupManager.getInstance().getData(groupId);

            if(groupData != null && groupData.hasForum()) {
                myGroups.add(GroupManager.getInstance().get(groupId));
            }
        }

        client.send(new GroupForumListMessageComposer(msg.readInt(), myGroups, client.getPlayer().getId()));
    }
}
