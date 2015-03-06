package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class TradeAcceptUpdateMessageComposer extends MessageComposer {
    private final int playerId;
    private final boolean accepted;

    public TradeAcceptUpdateMessageComposer(int playerId, boolean accepted) {
        this.playerId = playerId;
        this.accepted = accepted;
    }

    @Override
    public short getId() {
        return Composers.TradeUpdateMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(playerId);
        msg.writeInt(accepted ? 1 : 0);
    }
}
