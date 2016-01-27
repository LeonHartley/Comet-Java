package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupAccessLevel;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.group.GroupBadgesMessageComposer;
import com.cometproject.server.network.messages.outgoing.group.GroupRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.BadgeUtil;

import java.util.ArrayList;
import java.util.List;


public class BuyGroupMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (client.getPlayer().getData().getCredits() < CometSettings.groupCost) {
            return;
        }

        client.getPlayer().getData().decreaseCredits(CometSettings.groupCost);
        client.send(new SendCreditsMessageComposer(client.getPlayer().getData().getCredits()));
        client.getPlayer().getData().save();

        String name = msg.readString();
        String desc = msg.readString();

        int roomId = msg.readInt();
        int colour1 = msg.readInt();
        int colour2 = msg.readInt();

        if (!client.getPlayer().getRooms().contains(roomId) || RoomManager.getInstance().getRoomData(roomId) == null || GroupManager.getInstance().getGroupByRoomId(roomId) != null) {
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

        Group group = GroupManager.getInstance().createGroup(new GroupData(name, desc, badge, client.getPlayer().getId(), roomId, GroupManager.getInstance().getGroupItems().getSymbolColours().containsKey(colour1) ? colour1 : 1,
                GroupManager.getInstance().getGroupItems().getBackgroundColours().containsKey(colour2) ? colour2 : 1));

        group.getMembershipComponent().createMembership(new GroupMember(client.getPlayer().getId(), group.getId(), GroupAccessLevel.OWNER));
        client.getPlayer().getGroups().add(group.getId());

        client.getPlayer().getData().setFavouriteGroup(group.getId());
        client.getPlayer().getData().save();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom().getId() != roomId) {
            client.send(new RoomForwardMessageComposer(roomId));
        } else {
            Room room = client.getPlayer().getEntity().getRoom();

            room.setGroup(group);

            room.getData().setGroupId(group.getId());
            room.getData().save();

            room.getEntities().broadcastMessage(new GroupBadgesMessageComposer(group.getId(), group.getData().getBadge()));

            room.getEntities().broadcastMessage(new LeaveRoomMessageComposer(client.getPlayer().getEntity().getId()));
            room.getEntities().broadcastMessage(new AvatarsMessageComposer(client.getPlayer().getEntity()));
        }

        client.send(new GroupRoomMessageComposer(roomId, group.getId()));
    }
}
