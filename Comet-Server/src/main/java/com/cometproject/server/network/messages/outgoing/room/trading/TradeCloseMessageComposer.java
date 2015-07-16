package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class TradeCloseMessageComposer extends MessageComposer {
    private final int playerId;

    public TradeCloseMessageComposer(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.TradeCloseMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(playerId);
        msg.writeInt(2);
    }
}
