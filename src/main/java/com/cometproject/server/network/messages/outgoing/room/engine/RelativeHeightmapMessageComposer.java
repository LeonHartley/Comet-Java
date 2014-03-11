package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.game.rooms.types.RoomModel;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RelativeHeightmapMessageComposer {
    public static Composer compose(RoomModel model) {
        Composer msg = new Composer(Composers.RelativeHeightmapMessageComposer);

        model.composeRelative(msg);

        return msg;
    }
}
