package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupAccessLevel;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.SendPurchaseAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.group.NewGroupMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.ForwardRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.BadgeUtil;
import javolution.util.FastSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BuyGroupMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if (client.getPlayer().getData().getCredits() < CometSettings.groupCost) {
            return;
        }

        client.getPlayer().getData().decreaseCredits(CometSettings.groupCost);
        client.send(SendCreditsMessageComposer.compose(client.getPlayer().getData().getCredits()));
        client.getPlayer().getData().save();

        String name = msg.readString();
        String desc = msg.readString();

        int roomId = msg.readInt();
        int colour1 = msg.readInt();
        int colour2 = msg.readInt();

        if(!client.getPlayer().getRooms().contains(roomId) || CometManager.getRooms().getRoomData(roomId) == null) {
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

        System.out.println(badge + "," + groupBase + "," + groupBaseColour);

        client.send(BoughtItemMessageComposer.group());

        Group group = CometManager.getGroups().createGroup(new GroupData(name, desc, badge, client.getPlayer().getId(), roomId, CometManager.getGroups().getGroupItems().getSymbolColours().containsKey(colour1) ? colour1 : 1,
                CometManager.getGroups().getGroupItems().getBackgroundColours().containsKey(colour2) ? colour2 : 1, groupItems));

        group.getMembershipComponent().createMembership(new GroupMember(client.getPlayer().getId(), group.getId(), GroupAccessLevel.OWNER));
        client.getPlayer().getGroups().add(group.getId());

        client.send(ForwardRoomMessageComposer.compose(roomId));
        client.send(NewGroupMessageComposer.compose(roomId, group.getId()));

        client.getPlayer().getData().setFavouriteGroup(group.getId());
        client.getPlayer().getData().save();
    }
}
