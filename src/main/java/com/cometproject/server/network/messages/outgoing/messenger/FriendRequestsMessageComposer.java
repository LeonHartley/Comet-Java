package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class FriendRequestsMessageComposer {
    public static Composer compose(List<PlayerAvatar> requests) {
        Composer msg = new Composer(Composers.FriendRequestsMessageComposer);

        try {
            msg.writeInt(requests.size());
            msg.writeInt(requests.size());

            for (PlayerAvatar avatar : requests) {
                msg.writeInt(avatar.getId());
                msg.writeString(avatar.getUsername());
                msg.writeString(avatar.getFigure());
            }
        } finally {
            requests.clear();
        }

        return msg;
    }
}
