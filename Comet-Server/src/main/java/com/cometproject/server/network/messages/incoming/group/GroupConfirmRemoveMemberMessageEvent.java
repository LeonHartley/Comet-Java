package com.cometproject.server.network.messages.incoming.group;


import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.server.composers.group.GroupConfirmRemoveMemberMessageComposer;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Lists;

import java.util.List;

public class GroupConfirmRemoveMemberMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int playerId = msg.readInt();

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);
        if (group == null) {
            return;
        }

        if (client.getPlayer() == null) {
            return;
        }
        if (playerId == group.getData().getOwnerId()) {
            return;
        }

        IGroupMember groupMember = group.getMembers().getAll().get(client.getPlayer().getId());

        if (groupMember == null) {
            return;
        }

        if (playerId != client.getPlayer().getId()) {
            if (!groupMember.getAccessLevel().isAdmin()) {
                return;
            }
        }

        List<RoomItem> itemsToRemove = Lists.newArrayList();

        if (RoomManager.getInstance().isActive(group.getData().getRoomId())) {
            Room room = RoomManager.getInstance().get(group.getData().getRoomId());
            for (RoomItemFloor floorItem : room.getItems().getFloorItems().values()) {
                if (floorItem.getItemData().getOwnerId() == playerId) {
                    itemsToRemove.add(floorItem);
                }
            }
            for (RoomItemWall wallItem : room.getItems().getWallItems().values()) {
                if (wallItem.getItemData().getOwnerId() == playerId) {
                    itemsToRemove.add(wallItem);
                }
            }
        }

        client.send(new GroupConfirmRemoveMemberMessageComposer(playerId, itemsToRemove.size()));
        itemsToRemove.clear();
    }
}
