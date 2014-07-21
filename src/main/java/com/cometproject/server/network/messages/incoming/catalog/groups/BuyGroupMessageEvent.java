package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

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

        String badge = this.generateGuildImage(groupBase, groupBaseColour, groupItems);

        //client.send(BoughtItemMessageComposer.compose());
        //Group group = CometManager.getGroups().createGroup(name, desc, roomId, badge, client, CometManager.getGroups().getSymbolColours().containsKey(colour1) ? colour1 : 1, CometManager.getGroups().getBackgroundColours().containsKey(colour2) ? colour2 : 1);

        System.out.println(badge);
    }

    private String generateGuildImage(int guildBase, int guildBaseColor, List<Integer> guildStates) {
        String str = "";
        int num = 0;
        String str2 = "b";
        if (String.valueOf(guildBase).length() >= 2) {
            str2 = str2 + guildBase;
        } else {
            str2 = str2 + "0" + guildBase;
        }
        str = String.valueOf(guildBaseColor);
        if (str.length() >= 2) {
            str2 = str2 + str;
        } else if (str.length() <= 1) {
            str2 = str2 + "0" + str;
        }
        int num2 = 0;
        if (guildStates.get(9) != 0) {
            num2 = 4;
        } else if (guildStates.get(6) != 0) {
            num2 = 3;
        } else if (guildStates.get(3) != 0) {
            num2 = 2;
        } else if (guildStates.get(0) != 0) {
            num2 = 1;
        }
        int num3 = 0;
        for (int i = 0; i < num2; i++) {
            str2 = str2 + "s";
            num = guildStates.get(num3) - 20;
            if (String.valueOf(num).length() >= 2) {
                str2 = str2 + num;
            } else {
                str2 = str2 + "0" + num;
            }
            int num5 = guildStates.get(1 + num3);
            str = String.valueOf(num5);
            if (str.length() >= 2) {
                str2 = str2 + str;
            } else if (str.length() <= 1) {
                str2 = str2 + "0" + str;
            }
            str2 = str2 + guildStates.get(2 + num3).toString();
            switch (num3) {
                case 0:
                    num3 = 3;
                    break;

                case 3:
                    num3 = 6;
                    break;

                case 6:
                    num3 = 9;
                    break;
            }
        }
        return str2;
    }

}
