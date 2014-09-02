package com.cometproject.server.network.messages.outgoing.group.forums;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class GroupForumPermissionsMessageComposer {
    public static Composer compose(int groupId) {
        Composer msg = new Composer(Composers.GroupForumPermissionsMessageComposer);

        msg.writeInt(groupId);

        msg.writeInt(125); // ??
        msg.writeBoolean(true); // ??
        msg.writeBoolean(true); // ??
        msg.writeBoolean(true); // ??
        msg.writeBoolean(true); // ??

        return msg;
    }
}
