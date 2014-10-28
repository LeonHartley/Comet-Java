package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class InviteFriendMessageComposer {
    public static Composer compose(int userId, String message) {
        Composer msg = new Composer(Composers.ConsoleInvitationMessageComposer);

        msg.writeInt(userId);
        msg.writeString(message);

        return msg;
    }
}
