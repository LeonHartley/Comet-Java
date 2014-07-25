package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class NewGroupMessageComposer {
    public static Composer compose(int roomId, int groupId) {
        Composer msg = new Composer(Composers.NewGroupMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(groupId);

        return msg;
    }
}
