package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.game.moderation.chatlog.UserChatlogContainer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ModToolUserChatlogMessageComposer {
    public static Composer compose(int userId, UserChatlogContainer logContainer) {
        Composer msg = new Composer(Composers.ModToolUserChatlogMessageComposer);

        msg.writeInt(userId);
        msg.writeInt(logContainer.size());



        return msg;
    }
}
