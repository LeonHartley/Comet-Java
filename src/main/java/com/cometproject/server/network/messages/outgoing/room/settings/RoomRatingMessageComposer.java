package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomRatingMessageComposer {
    public static Composer compose(int score, boolean canRate) {
        Composer msg = new Composer(Composers.RoomRatingMessageComposer);

        msg.writeInt(score);
        msg.writeBoolean(canRate);

        return msg;
    }
}
