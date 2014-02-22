package com.cometproject.network.messages.outgoing.room.avatar;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class DanceMessageComposer {
    public static Composer compose(int playerId, int danceId) {
        Composer msg = new Composer(Composers.DanceMessageComposer);

        msg.writeInt(playerId);
        msg.writeInt(danceId);

        return msg;
    }
}
