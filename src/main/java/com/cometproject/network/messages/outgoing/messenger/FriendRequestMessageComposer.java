package com.cometproject.network.messages.outgoing.messenger;

import com.cometproject.game.players.components.types.MessengerRequest;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class FriendRequestMessageComposer {
    public static Composer compose(MessengerRequest request) {
        Composer msg = new Composer(Composers.RequestFriendshipMessageComposer);

        msg.writeInt(request.getFromId());
        msg.writeString(request.getUsername());
        msg.writeString(request.getLook());

        return msg;
    }
}
