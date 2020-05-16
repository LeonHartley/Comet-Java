package com.cometproject.server.network.messages.incoming.group.settings;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.GroupType;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.YouAreControllerMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class ModifyGroupSettingsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        if (!client.getPlayer().getGroups().contains(groupId))
            return;

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null || group.getData().getOwnerId() != client.getPlayer().getId())
            return;

        int type = msg.readInt();
        int rightsType = msg.readInt();

        if (GroupType.valueOf(type) != group.getData().getType()) {
            GameContext.getCurrent().getGroupService().clearRequests(group);
        }

        group.getData().setType(GroupType.valueOf(type));

        // 0 = members, 1 = admins only.
        group.getData().setCanMembersDecorate(rightsType == 0);

        if(group.getForum() != null) {
            GameContext.getCurrent().getGroupService().saveForumSettings(group.getForum().getForumSettings());
        }

        if (RoomManager.getInstance().isActive(group.getData().getRoomId())) {
            Room room = RoomManager.getInstance().get(group.getData().getRoomId());

//            room.getEntities().broadcastMessage(new RoomInfoUpdatedMessageComposer(room.getId()));
//            room.getEntities().broadcastMessage(new SettingsUpdatedMessageComposer(room.getId()));

            if (group.getData().canMembersDecorate()) {
                for (IGroupMember groupMember : group.getMembers().getAll().values()) {
                    PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(groupMember.getPlayerId());

                    if (playerEntity != null) {
                        playerEntity.removeStatus(RoomEntityStatus.CONTROLLER);
                        playerEntity.addStatus(RoomEntityStatus.CONTROLLER, "1");

                        playerEntity.markNeedsUpdate();
                        playerEntity.getPlayer().getSession().send(new YouAreControllerMessageComposer(1));
                    }
                }

            }
            room.getEntities().broadcastMessage(new RoomDataMessageComposer(room, false, false, false));
        }
    }
}
