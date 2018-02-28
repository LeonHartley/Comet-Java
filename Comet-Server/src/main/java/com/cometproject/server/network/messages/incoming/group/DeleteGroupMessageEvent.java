package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.storage.api.StorageContext;
import com.google.common.collect.Lists;

import java.util.List;

public class DeleteGroupMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null) {
            return;
        }

        Room room = RoomManager.getInstance().get(group.getData().getRoomId());

        if (group.getData().getOwnerId() != client.getPlayer().getId()) {
            return;
        }

        for (Integer groupMemberId : group.getMembers().getAll().keySet()) {
            Session groupMemberSession = NetworkManager.getInstance().getSessions().getByPlayerId(groupMemberId);

            List<RoomItem> floorItemsOwnedByPlayer = Lists.newArrayList();

            if (groupMemberId != group.getData().getOwnerId()) {
                for (RoomItemFloor floorItem : room.getItems().getFloorItems().values()) {
                    if (floorItem.getItemData().getOwnerId() == groupMemberId) {
                        floorItemsOwnedByPlayer.add(floorItem);
                    }
                }

                for (RoomItemWall wallItem : room.getItems().getWallItems().values()) {
                    if (wallItem.getItemData().getOwnerId() == groupMemberId) {
                        floorItemsOwnedByPlayer.add(wallItem);
                    }
                }
            }

            if (groupMemberSession != null && groupMemberSession.getPlayer() != null) {
                groupMemberSession.getPlayer().getGroups().remove(group.getId());

                if (groupMemberSession.getPlayer().getData().getFavouriteGroup() == group.getId()) {
                    groupMemberSession.getPlayer().getData().setFavouriteGroup(0);
                }

                for (RoomItem roomItem : floorItemsOwnedByPlayer) {
                    if (roomItem instanceof RoomItemFloor)
                        room.getItems().removeItem(((RoomItemFloor) roomItem), groupMemberSession);
                    else if (roomItem instanceof RoomItemWall)
                        room.getItems().removeItem(((RoomItemWall) roomItem), groupMemberSession, true);
                }
            } else {
                for (RoomItem roomItem : floorItemsOwnedByPlayer) {
                    StorageContext.getCurrentContext().getRoomItemRepository().removeItemFromRoom(roomItem.getId(), groupMemberId, roomItem.getItemData().getData());
                }
            }

            floorItemsOwnedByPlayer.clear();
        }

        client.send(new AlertMessageComposer(Locale.getOrDefault("command.deletegroup.done", "The group was deleted successfully.")));
        GameContext.getCurrent().getGroupService().removeGroup(group.getId());

        room.setGroup(null);

        room.getData().setGroupId(0);

        GameContext.getCurrent().getRoomService().saveRoomData(room.getData());

        room.setIdleNow();
    }
}
