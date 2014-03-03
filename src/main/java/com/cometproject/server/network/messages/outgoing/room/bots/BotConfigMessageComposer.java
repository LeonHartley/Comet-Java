package com.cometproject.server.network.messages.outgoing.room.bots;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class BotConfigMessageComposer {
    public static Composer compose(int botId, int skill, String message) {
        Composer msg = new Composer(Composers.BotConfigMessageComposer);

        msg.writeInt(botId);
        msg.writeInt(skill);
        msg.writeString(message);

        return msg;
    }
}
