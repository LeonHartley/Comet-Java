package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class GroupFurnitureWidgetMessageComposer {
    public static Composer compose(int furnitureId, int groupId, String groupTitle, int homeRoom, boolean isMember, boolean hasForum) {
        Composer msg = new Composer(Composers.GroupFurnitureWidgetMessageComposer);

        msg.writeInt(furnitureId);
        msg.writeInt(groupId);
        msg.writeString(groupTitle);
        msg.writeInt(homeRoom);
        msg.writeBoolean(isMember);//??
        msg.writeBoolean(hasForum);

        return msg;
    }
}
