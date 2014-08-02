package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.GroupMembersMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GroupMembersMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();
        int page = msg.readInt();
        String searchQuery = msg.readString();
        int requestType = msg.readInt();

        Group group = CometManager.getGroups().get(groupId);

        if(group == null)
            return;

        client.send(GroupMembersMessageComposer.compose(group, page, requestType, searchQuery, client.getPlayer().getId() == group.getData().getOwnerId()));
    }
}
