package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.BadgeUtil;

import java.util.ArrayList;
import java.util.List;

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

        int stateCount = msg.readInt();

        int groupBase = msg.readInt();
        int groupBaseColour = msg.readInt();
        int groupItemsLength = msg.readInt() * 3;

        List<Integer> groupItems = new ArrayList<>();

        for (int i = 0; i < (groupItemsLength); i++) {
            groupItems.add(msg.readInt());
        }

        String badge = BadgeUtil.generate(groupBase, groupBaseColour, groupItems);

        //client.send(BoughtItemMessageComposer.compose());
        //Group group = CometManager.getGroups().createGroup(name, desc, roomId, badge, client, CometManager.getGroups().getSymbolColours().containsKey(colour1) ? colour1 : 1, CometManager.getGroups().getBackgroundColours().containsKey(colour2) ? colour2 : 1);

        System.out.println(badge);
    }
}
