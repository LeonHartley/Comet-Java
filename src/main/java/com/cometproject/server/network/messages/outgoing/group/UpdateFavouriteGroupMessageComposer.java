package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class UpdateFavouriteGroupMessageComposer extends MessageComposer {
    private final int playerId;

    public UpdateFavouriteGroupMessageComposer(final int playerId) {
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.ChangeFavouriteGroupMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.playerId);
    }
}
