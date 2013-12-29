package com.cometsrv.network.messages.outgoing.messenger;

import com.cometsrv.game.players.components.types.MessengerRequest;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class FriendRequestMessageComposer {
    public static Composer compose(MessengerRequest request) {
        Composer msg = new Composer(Composers.RequestFriendshipMessageComposer);

        msg.writeInt(request.getFromId());
        msg.writeString(request.getUsername());
        msg.writeString(request.getLook());

        return msg;
    }
}
