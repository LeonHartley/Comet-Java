package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class UpdateFavouriteGroupMessageComposer {
    public static Composer compose(int playerId) {
        Composer msg = new Composer(Composers.UpdateFavouriteGroupMessageComposer);

        msg.writeInt(playerId);

        return msg;
    }
}
