package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupAccessLevel;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.GroupMembersMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;


public class GiveGroupAdminMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int playerId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId)) {
            return;
        }

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null)
            return;

        if (!group.getMembershipComponent().getMembers().containsKey(playerId))
            return;

        GroupMember groupMember = group.getMembershipComponent().getMembers().get(playerId);

        if (groupMember == null)
            return;

        if (groupMember.getAccessLevel().isAdmin())
            return;

        groupMember.setAccessLevel(GroupAccessLevel.ADMIN);
        groupMember.save();

        group.getMembershipComponent().getAdministrators().add(groupMember.getPlayerId());

        Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

        if (session != null) {
            if (session.getPlayer() != null && session.getPlayer().getEntity() != null && session.getPlayer().getEntity().getRoom() != null) {
                session.getPlayer().getEntity().removeStatus(RoomEntityStatus.CONTROLLER);
                session.getPlayer().getEntity().addStatus(RoomEntityStatus.CONTROLLER, "1");

                session.getPlayer().getEntity().markNeedsUpdate();
                session.getPlayer().getEntity().getPlayer().getSession().send(new AccessLevelMessageComposer(1));

            }
        }

        client.send(new GroupMembersMessageComposer(group.getData(), 0, new ArrayList<>(group.getMembershipComponent().getAdministrators()), 1, "", group.getMembershipComponent().getAdministrators().contains(client.getPlayer().getId())));
    }
}
