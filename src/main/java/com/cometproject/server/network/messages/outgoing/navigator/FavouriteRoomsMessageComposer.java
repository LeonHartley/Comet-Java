package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class FavouriteRoomsMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.FavouriteRoomsMessageComposer);

        // TODO: Favourite rooms

        msg.writeInt(30);// max favourite rooms
        msg.writeInt(0);//size

        return msg;
    }
}
