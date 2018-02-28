package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;


public class FriendRequestsMessageComposer extends MessageComposer {
    private final List<PlayerAvatar> requests;

    public FriendRequestsMessageComposer(final List<PlayerAvatar> requests) {
        this.requests = requests;
    }

    @Override
    public short getId() {
        return Composers.BuddyRequestsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.requests.size());
        msg.writeInt(this.requests.size());

        for (PlayerAvatar avatar : this.requests) {
            msg.writeInt(avatar.getId());
            msg.writeString(avatar.getUsername());
            msg.writeString(avatar.getFigure());
        }
    }

    @Override
    public void dispose() {
        this.requests.clear();
    }
}
