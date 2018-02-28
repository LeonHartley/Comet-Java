package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.membership.GroupAccessLevel;
import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.server.composers.group.GroupMembersMessageComposer;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.permissions.YouAreControllerMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.storage.mysql.models.factories.GroupMemberFactory;


public class AcceptMembershipMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int playerId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId))
            return;

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null || (group.getData().getOwnerId() != client.getPlayer().getId() &&
                !group.getMembers().getAdministrators().contains(client.getPlayer().getId()))) {
            return;
        }

        if (!group.getMembers().getMembershipRequests().contains(playerId))
            return;

        GameContext.getCurrent().getGroupService().removeRequest(group, playerId);
        GameContext.getCurrent().getGroupService().addGroupMember(group, new GroupMemberFactory().create(playerId, groupId, GroupAccessLevel.MEMBER));

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


        client.send(new GroupMembersMessageComposer(group.getData(), 0,
                group.getMembers().getRequestAvatars(), 2, "",
                true));
    }
}
