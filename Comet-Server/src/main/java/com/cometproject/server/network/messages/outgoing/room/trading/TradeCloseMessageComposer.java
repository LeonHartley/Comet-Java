package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


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
