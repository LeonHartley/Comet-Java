package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.server.composers.group.GroupDataMessageComposer;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.List;


public class GroupFurnitureCatalogMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final List<IGroupData> groupData = Lists.newArrayList();

        for(Integer groupId : client.getPlayer().getGroups()) {
            final IGroupData data = GroupManager.getInstance().getData(groupId);

            if(data != null) {
                groupData.add(data);
            }
        }

        client.send(new GroupDataMessageComposer(groupData, GroupManager.getInstance().getGroupItems(),
                client.getPlayer().getId()));
    }
}
