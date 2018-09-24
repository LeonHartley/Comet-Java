package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.IGroupItemService;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.server.composers.catalog.BoughtItemMessageComposer;
import com.cometproject.server.composers.group.GroupBadgesMessageComposer;
import com.cometproject.server.composers.group.GroupRoomMessageComposer;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.UpdateFriendStateMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.RemoveRightsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.YouAreControllerMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.utilities.BadgeUtil;
import com.cometproject.storage.mysql.models.factories.GroupDataFactory;

import java.util.ArrayList;
import java.util.List;


public class BuyGroupMessageEvent implements Event {

    public void handle(Session client, MessageEvent msg) {
        if (client.getPlayer().getData().getCredits() < CometSettings.groupCost) {
            return;
        }

        client.getPlayer().getData().decreaseCredits(CometSettings.groupCost);
        client.send(new SendCreditsMessageComposer(Integer.toString(client.getPlayer().getData().getCredits())));
        client.getPlayer().getData().save();

        String name = msg.readString();
        String desc = msg.readString();

        int roomId = msg.readInt();
        int colour1 = msg.readInt();
        int colour2 = msg.readInt();

        if (!client.getPlayer().getRooms().contains(roomId) || GameContext.getCurrent().getRoomService().getRoomData(roomId) == null) {
            return;
        }

        int stateCount = msg.readInt();

        int groupBase = msg.readInt();
        int groupBaseColour = msg.readInt();
        int groupItemsLength = msg.readInt() * 3;

        List<Integer> groupItems = new ArrayList<>();

        for (int i = 0; i < (groupItemsLength); i++) {
            groupItems.add(msg.readInt());
        }

        String badge = BadgeUtil.generate(groupBase, groupBaseColour, groupItems);

        client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.GROUP));

        final IGroupItemService itemService = GameContext.getCurrent().getGroupService().getItemService();

        final IGroupData groupData = new GroupDataFactory().create(name, desc, badge, client.getPlayer().getId(),
                client.getPlayer().getData().getUsername(), roomId, colour1, colour2, client.getPlayer().getData());
        final IGroup group = GameContext.getCurrent().getGroupService().createGroup(groupData, client.getPlayer().getId());

//        group.getMembers().createMembership(new GroupMemberFactory().create(client.getPlayer().getId(), group.getId(), GroupAccessLevel.OWNER));*/
        client.getPlayer().getGroups().add(group.getId());

        client.getPlayer().getData().setFavouriteGroup(group.getId());
        client.getPlayer().getData().save();

        final Room room = RoomManager.getInstance().get(roomId);

        if (room != null) {
            if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() != null &&
                    client.getPlayer().getEntity().getRoom().getId() != roomId) {

                client.send(new RoomForwardMessageComposer(roomId));
            }

            if (room.getData().getOwnerId() != client.getPlayer().getId()) {
                return;
            }

            List<Integer> toRemove = new ArrayList<>();

            for (Integer id : room.getRights().getAll()) {
                PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(id);

                if (playerEntity != null) {
                    playerEntity.getPlayer().getSession().send(new YouAreControllerMessageComposer(0));
                }

                // Remove rights from the player id
                client.send(new RemoveRightsMessageComposer(id, room.getId()));
                toRemove.add(id);
            }

            for (Integer id : toRemove) {
                room.getRights().removeRights(id);
            }

            toRemove.clear();

            room.setGroup(group);

            room.getData().setGroupId(group.getId());

            GameContext.getCurrent().getRoomService().saveRoomData(room.getData());

            room.getEntities().broadcastMessage(new GroupBadgesMessageComposer(group.getId(), group.getData().getBadge()));

            room.getEntities().broadcastMessage(new LeaveRoomMessageComposer(client.getPlayer().getEntity().getId()));
            room.getEntities().broadcastMessage(new AvatarsMessageComposer(client.getPlayer().getEntity()));

            if (CometSettings.groupChatEnabled) {
                client.send(new UpdateFriendStateMessageComposer(group.getData()));
            }

            client.send(new GroupRoomMessageComposer(roomId, group.getId()));
        }
    }
}
