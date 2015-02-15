package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class MessengerConfigMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.LoadFriendsMessageComposer);

        msg.writeInt(1100); // TODO: put this stuff in static config somewhere :P
        msg.writeInt(300);
        msg.writeInt(800);
        msg.writeInt(1100);
        msg.writeInt(0);

        return msg;
    }
}
