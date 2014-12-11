package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class GroupDataMessageComposer {
    public static Composer compose(List<Integer> groups, int userId) {
        Composer msg = new Composer(Composers.GroupFurniturePageMessageComposer);

        int count = 0;

        for (Integer groupId : groups) {
            if (GroupManager.getInstance().getData(groupId) != null) count++;
        }

        msg.writeInt(count);

        for (Integer groupId : groups) {
            GroupData group = GroupManager.getInstance().getData(groupId);

            if (group != null) {
                msg.writeInt(group.getId());
                msg.writeString(group.getTitle());
                msg.writeString(group.getBadge());

                String colourA = GroupManager.getInstance().getGroupItems().getSymbolColours().get(group.getColourA()) != null ? GroupManager.getInstance().getGroupItems().getSymbolColours().get(group.getColourA()).getColour() : "ffffff";
                String colourB = GroupManager.getInstance().getGroupItems().getBackgroundColours().get(group.getColourB()) != null ? GroupManager.getInstance().getGroupItems().getBackgroundColours().get(group.getColourB()).getColour() : "ffffff";

                msg.writeString(colourA);
                msg.writeString(colourB);

                msg.writeBoolean(group.getOwnerId() == userId);
                msg.writeInt(group.getOwnerId());
                msg.writeBoolean(false); // has forum
            }
        }

        return msg;
    }
}