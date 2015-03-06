package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class DanceMessageComposer extends MessageComposer {
    private final int playerId;
    private final int danceId;

    public DanceMessageComposer(final int playerId, final int danceId) {
        this.playerId = playerId;
        this.danceId = danceId;
    }

    @Override
    public short getId() {
        return Composers.DanceStatusMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(playerId);
        msg.writeInt(danceId);
    }
}
