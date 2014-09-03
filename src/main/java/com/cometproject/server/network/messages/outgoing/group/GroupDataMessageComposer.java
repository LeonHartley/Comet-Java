package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class GroupDataMessageComposer {
    public static Composer compose(List<Integer> groups, int favouriteGroup) {
        Composer msg = new Composer(Composers.GroupDataMessageComposer);

        int count = 0;

        for(Integer groupId : groups) {
            if(CometManager.getGroups().getData(groupId) != null)  count++;
        }

        msg.writeInt(count);

        for(Integer groupId : groups) {
            GroupData group = CometManager.getGroups().getData(groupId);

            if(group != null) {
                msg.writeInt(group.getId());
                msg.writeString(group.getTitle());
                msg.writeString(group.getBadge());
                msg.writeString(CometManager.getGroups().getGroupItems().getBackgroundColour(group.getColourA()));
                msg.writeString(CometManager.getGroups().getGroupItems().getBackgroundColour(group.getColourB()));
                msg.writeBoolean(group.getId() == favouriteGroup);
            }
        }

        return msg;
    }
}
