package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class TradeStartMessageComposer extends MessageComposer {
    private final int user1;
    private final int user2;

    public TradeStartMessageComposer(int user1, int user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    @Override
    public short getId() {
        return Composers.TradeStartMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(user1);
        msg.writeInt(1);
        msg.writeInt(user2);
        msg.writeInt(1);
    }
}
