package com.cometproject.gamecenter.fastfood.net.composers;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;

public class UseBigParachuteMessageComposer extends MessageComposer {
    private final int playerId;
    private final int objectId;

    public UseBigParachuteMessageComposer(int playerId, int objectId) {
        this.playerId = playerId;
        this.objectId = objectId;
    }

    @Override
    public short getId() {
        return 9;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.objectId);
        msg.writeInt(this.playerId);
    }
}
