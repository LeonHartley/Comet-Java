package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.GroupType;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.membership.GroupAccessLevel;
import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.server.composers.group.GroupBadgesMessageComposer;
import com.cometproject.server.composers.group.GroupInformationMessageComposer;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.UpdateFriendStateMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.YouAreControllerMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.storage.mysql.models.factories.GroupMemberFactory;


public class JoinGroupMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        if (client.getPlayer().getGroups().size() >= 100) {
            return;
        }

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null || group.getData().getType() == GroupType.PRIVATE) {
            return;
        }

        if (client.getPlayer().getGroups().contains(groupId) && group.getMembers().hasMembership(client.getPlayer().getId())) {
            // Already joined, what you doing??
            return;
        }

        if (group.getData().getType() == GroupType.REGULAR) {
            if (client.getPlayer().getData().getFavouriteGroup() == 0) {
                client.getPlayer().getData().setFavouriteGroup(groupId);
                client.getPlayer().getData().save();

                if (client.getPlayer().getEntity() != null) {
                    client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new GroupBadgesMessageComposer(groupId, group.getData().getBadge()));
                }
            }

            client.getPlayer().getGroups().add(groupId);

            GameContext.getCurrent().getGroupService().addGroupMember(group, new GroupMemberFactory().create(client.getPlayer().getId(), group.getId(), GroupAccessLevel.MEMBER));

            client.send(new GroupInformationMessageComposer(group, GameContext.getCurrent().getRoomService().getRoomData(group.getData().getRoomId()), false,
                    client.getPlayer().getId() == group.getData().getOwnerId(), group.getMembers().getAdministrators().contains(client.getPlayer().getId()),
                    group.getMembers().getAll().containsKey(client.getPlayer().getId()) ? 1 : group.getMembers().getMembershipRequests().contains(client.getPlayer().getId()) ? 2 : 0));

            if (CometSettings.groupChatEnabled) {
                client.send(new UpdateFriendStateMessageComposer(group.getData()));
            }

            if (client.getPlayer().getEntity() != null && group.getData().canMembersDecorate()) {
                client.getPlayer().getEntity().removeStatus(RoomEntityStatus.CONTROLLER);
                client.getPlayer().getEntity().addStatus(RoomEntityStatus.CONTROLLER, "1");

                client.getPlayer().getEntity().markNeedsUpdate();
                client.send(new YouAreControllerMessageComposer(1));
            }
        } else {
            GameContext.getCurrent().getGroupService().createRequest(group, client.getPlayer().getId());

            client.send(new GroupInformationMessageComposer(group, GameContext.getCurrent().getRoomService().getRoomData(group.getData().getRoomId()), true,
                    client.getPlayer().getId() == group.getData().getOwnerId(), group.getMembers().getAdministrators().contains(client.getPlayer().getId()),
                    group.getMembers().getAll().containsKey(client.getPlayer().getId()) ? 1 : group.getMembers().getMembershipRequests().contains(client.getPlayer().getId()) ? 2 : 0));

        }
    }
}
