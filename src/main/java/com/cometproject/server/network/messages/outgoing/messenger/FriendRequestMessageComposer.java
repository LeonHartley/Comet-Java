package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class FriendRequestMessageComposer {
    public static Composer compose(PlayerAvatar avatar) {
        Composer msg = new Composer(Composers.ConsoleSendFriendRequestMessageComposer);

        msg.writeInt(avatar.getId());
        msg.writeString(avatar.getUsername());
        msg.writeString(avatar.getFigure());

        return msg;
    }
}
