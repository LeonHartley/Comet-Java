package com.cometproject.server.composers.gamecenter;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class GameStatusMessageComposer extends MessageComposer {

    private final int gameTypeId;
    private final int status;

    public GameStatusMessageComposer(int gameTypeId, int status) {
        this.gameTypeId = gameTypeId;
        this.status = status;
    }

    @Override
    public short getId() {
        return Composers.GameStatusMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(gameTypeId);
        msg.writeInt(status);
    }
}
