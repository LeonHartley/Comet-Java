package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class FollowFriendMessageComposer {
    public static Composer compose(int roomId) {
        Composer msg = new Composer(Composers.FollowBuddyMessageComposer);

        msg.writeBoolean(false); // is public room - public rooms don't exist anymore.
        msg.writeInt(roomId);

        return msg;
    }
}
