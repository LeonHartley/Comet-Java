package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.membership.GroupAccessLevel;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.server.composers.group.GroupMembersMessageComposer;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.permissions.YouAreControllerMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.storage.api.StorageContext;


public class GiveGroupAdminMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int playerId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId)) {
            return;
        }

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null || group.getData() == null || group.getData().getOwnerId() != client.getPlayer().getId())
            return;

        if (!group.getMembers().getAll().containsKey(playerId))
            return;

        IGroupMember groupMember = group.getMembers().getAll().get(playerId);

        if (groupMember == null)
            return;

        if (groupMember.getAccessLevel().isAdmin())
            return;

        groupMember.setAccessLevel(GroupAccessLevel.ADMIN);

        StorageContext.getCurrentContext().getGroupRepository().saveGroupData(group.getData());

        group.getMembers().getAdministrators().add(groupMember.getPlayerId());

        Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

        if (session != null) {
            if (session.getPlayer() != null && session.getPlayer().getEntity() != null && session.getPlayer().getEntity().getRoom() != null) {
                session.getPlayer().getEntity().removeStatus(RoomEntityStatus.CONTROLLER);
                session.getPlayer().getEntity().addStatus(RoomEntityStatus.CONTROLLER, "1");

                session.getPlayer().getEntity().markNeedsUpdate();
                session.getPlayer().getEntity().getPlayer().getSession().send(new YouAreControllerMessageComposer(1));

            }
        }

        client.send(new GroupMembersMessageComposer(group.getData(), 0, group.getMembers().getAdminAvatars(), 1, "", group.getMembers().getAdministrators().contains(client.getPlayer().getId())));
    }
}
