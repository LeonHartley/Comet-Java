package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupAccessLevel;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.GroupMembersMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.YouAreControllerMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;


public class AcceptMembershipMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int playerId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId))
            return;

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null || (group.getData().getOwnerId() != client.getPlayer().getId() && !group.getMembershipComponent().getAdministrators().contains(client.getPlayer().getId()))) {
            return;
        }

        if (!group.getMembershipComponent().getMembershipRequests().contains(playerId))
            return;

        group.getMembershipComponent().removeRequest(playerId);
        group.getMembershipComponent().createMembership(new GroupMember(playerId, groupId, GroupAccessLevel.MEMBER));

        Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

        if (session != null && session.getPlayer() != null) {
            session.getPlayer().getGroups().add(groupId);

            if (group.getData().canMembersDecorate()) {
                if (session.getPlayer().getEntity() != null && group.getData().canMembersDecorate()) {
                    session.getPlayer().getEntity().removeStatus(RoomEntityStatus.CONTROLLER);
                    session.getPlayer().getEntity().addStatus(RoomEntityStatus.CONTROLLER, "1");

                    session.getPlayer().getEntity().markNeedsUpdate();
                    session.send(new YouAreControllerMessageComposer(1));
                }
            }
        }


        client.send(new GroupMembersMessageComposer(group.getData(), 0, new ArrayList<>(group.getMembershipComponent().getMembershipRequests()), 2, "", true));
    }
}
