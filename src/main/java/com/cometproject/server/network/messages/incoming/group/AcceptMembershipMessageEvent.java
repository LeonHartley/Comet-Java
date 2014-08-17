package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupAccessLevel;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.GroupMembersMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;

public class AcceptMembershipMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();
        int playerId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId))
            return;

        Group group = CometManager.getGroups().get(groupId);

        if (group == null || group.getData().getOwnerId() != client.getPlayer().getId())
            return;

        if (!group.getMembershipComponent().getMembershipRequests().contains(playerId))
            return;

        group.getMembershipComponent().removeRequest(playerId);
        group.getMembershipComponent().createMembership(new GroupMember(playerId, groupId, GroupAccessLevel.MEMBER));

        client.send(GroupMembersMessageComposer.compose(group.getData(), 0, new ArrayList<>(group.getMembershipComponent().getMembershipRequests()), 2, "", true));
    }
}
