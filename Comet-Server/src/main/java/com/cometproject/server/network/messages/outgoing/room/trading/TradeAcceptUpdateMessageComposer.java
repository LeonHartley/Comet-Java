package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class TradeAcceptUpdateMessageComposer extends MessageComposer {
    private final int playerId;
    private final boolean accepted;

    public TradeAcceptUpdateMessageComposer(int playerId, boolean accepted) {
        this.playerId = playerId;
        this.accepted = accepted;
    }

    @Override
    public short getId() {
        return Composers.TradingAcceptMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(playerId);
        msg.writeInt(accepted ? 1 : 0);
    }
}
