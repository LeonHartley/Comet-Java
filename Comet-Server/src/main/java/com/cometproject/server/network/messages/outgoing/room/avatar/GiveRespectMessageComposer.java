package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class GiveRespectMessageComposer extends MessageComposer {
    private final int playerId;
    private final int totalRespects;

    public GiveRespectMessageComposer(final int playerId, final int totalRespects) {
        this.playerId = playerId;
        this.totalRespects = totalRespects;
    }

    @Override
    public short getId() {
        return Composers.RespectNotificationMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(playerId);
        msg.writeInt(totalRespects);
    }
}
