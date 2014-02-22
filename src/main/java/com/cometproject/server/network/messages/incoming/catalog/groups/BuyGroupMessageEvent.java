package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastList;

import java.util.List;

public class BuyGroupMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if(client.getPlayer().getData().getCredits() < CometSettings.groupCost) {
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

        msg.readInt();
        int groupBase = msg.readInt();
        int groupBaseColour = msg.readInt();
        int groupItemsLength = msg.readInt();

        List<Integer> groupItems = new FastList<>();

        for(int i = 0; i < (groupItemsLength * 3); i++) {
            groupItems.add(msg.readInt());
        }

        String badge = GameEngine.getGroups().generateBadge(groupBase, groupBaseColour, groupItems);
       /* int groupBase4 = msg.readInt();
        int groupBase5 = msg.readInt();
        int groupBase6 = msg.readInt();
        int groupBase7 = msg.readInt();
        int groupBase8 = msg.readInt();
        int groupBase9 = msg.readInt();
        int groupBase10 = msg.readInt();
        int groupBase11 = msg.readInt();
        int groupBase12 = msg.readInt();
        int groupBase13 = msg.readInt();
        int groupBase14 = msg.readInt();
        int groupBase15 = msg.readInt();
        int groupBase16 = msg.readInt();
        int groupBase17 = msg.readInt();
        int groupBase18 = msg.readInt();

        String base = "b" + ((groupBase4 < 10) ? "0" + groupBase4 : groupBase4) + ((groupBase5 < 10) ? "0" + groupBase5 : groupBase5) + groupBase6;
        String s1 = GameEngine.getGroups().checkSymbol("s" + ((groupBase7 < 10) ? "0" + groupBase7 : groupBase7) + ((groupBase8 < 10) ? "0" + groupBase8 : groupBase8) + groupBase9);
        String s2 = GameEngine.getGroups().checkSymbol("s" + ((groupBase10 < 10) ? "0" + groupBase10 : groupBase10) + ((groupBase11 < 10) ? "0" + groupBase11 : groupBase11) + groupBase12);
        String s3 = GameEngine.getGroups().checkSymbol("s" + ((groupBase13 < 10) ? "0" + groupBase13 : groupBase13) + ((groupBase14 < 10) ? "0" + groupBase14 : groupBase14) + groupBase15);
        String s4 = GameEngine.getGroups().checkSymbol("s" + ((groupBase16 < 10) ? "0" + groupBase16 : groupBase16) + ((groupBase17 < 10) ? "0" + groupBase17 : groupBase17) + groupBase18);

        String badge = base + s1 + s2 + s2 + s3 + s4;*/

        client.send(BoughtItemMessageComposer.compose());
        Group group = GameEngine.getGroups().createGroup(name, desc, roomId, badge, client, GameEngine.getGroups().getSymbolColours().containsKey(colour1) ? colour1 : 1, GameEngine.getGroups().getBackgroundColours().containsKey(colour2) ? colour2 : 1);
    }
}
