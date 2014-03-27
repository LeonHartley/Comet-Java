package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.components.types.MessengerRequest;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class FriendRequestsMessageComposer {
    public static Composer compose(List<MessengerRequest> requests) {
        Composer msg = new Composer(Composers.FriendRequestsMessageComposer);

        msg.writeInt(requests.size());
        msg.writeInt(requests.size());

        for(MessengerRequest request : requests) {
            msg.writeInt(request.getFromId());
            msg.writeString(request.getUsername());
            msg.writeString(request.getLook());
        }

        return msg;
    }
}
