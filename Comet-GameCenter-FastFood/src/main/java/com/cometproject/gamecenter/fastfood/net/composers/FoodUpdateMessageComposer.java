package com.cometproject.gamecenter.fastfood.net.composers;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;

public class FoodUpdateMessageComposer extends MessageComposer {
    private int playerId;
    private int objectId;
    private int state;
    private int test;
    private int test2;

    public FoodUpdateMessageComposer(int playerId, int objectId, int state, int test, int test2) {
        this.playerId = playerId;
        this.objectId = objectId;
        this.state = state;
        this.test = test;
        this.test2 = test2;
    }

    @Override
    public short getId() {
        return 5;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(objectId);
        msg.writeInt(playerId);
        msg.writeInt(state);
        msg.writeInt(test);
        msg.writeInt(test2);
    }
}
