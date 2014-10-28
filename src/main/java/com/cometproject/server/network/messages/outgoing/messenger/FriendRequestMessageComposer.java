package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.components.types.MessengerRequest;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class FriendRequestMessageComposer {
    public static Composer compose(MessengerRequest request) {
        Composer msg = new Composer(Composers.FriendRequestsMessageComposer);

        msg.writeInt(request.getFromId());
        msg.writeString(request.getUsername());
        msg.writeString(request.getLook());

        return msg;
    }
}
