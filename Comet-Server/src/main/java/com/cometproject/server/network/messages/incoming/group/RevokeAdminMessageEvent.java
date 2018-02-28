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


public class RevokeAdminMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int playerId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId)) {
            return;
        }

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null)
            return;

        if (!group.getMembers().getAll().containsKey(playerId))
            return;

        IGroupMember groupMember = group.getMembers().getAll().get(playerId);

        if (groupMember == null)
            return;

        if (!groupMember.getAccessLevel().isAdmin())
            return;

        groupMember.setAccessLevel(GroupAccessLevel.MEMBER);
        StorageContext.getCurrentContext().getGroupMemberRepository().saveMember(groupMember);

        if (!group.getData().canMembersDecorate()) {
            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(groupMember.getPlayerId());

            if (session != null && session.getPlayer().getEntity() != null && session.getPlayer().getEntity().getRoom().getId() == group.getData().getRoomId()) {
                session.send(new YouAreControllerMessageComposer(0));
                session.getPlayer().getEntity().removeStatus(RoomEntityStatus.CONTROLLER);
                session.getPlayer().getEntity().addStatus(RoomEntityStatus.CONTROLLER, "0");
                session.getPlayer().getEntity().markNeedsUpdate();
            }
        }

        group.getMembers().getAdministrators().remove(groupMember.getPlayerId());
        client.send(new GroupMembersMessageComposer(group.getData(), 0, group.getMembers().getAdminAvatars(), 1, "", group.getMembers().getAdministrators().contains(client.getPlayer().getId())));
    }
}
