package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class GiveRespectMessageComposer extends MessageComposer {
    private final int playerId;
    private final int totalRespects;

    public GiveRespectMessageComposer(final int playerId, final int totalRespects) {
        this.playerId = playerId;
        this.totalRespects = totalRespects;
    }

    @Override
    public short getId() {
        return Composers.GiveRespectsMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(playerId);
        msg.writeInt(totalRespects);
    }
}
