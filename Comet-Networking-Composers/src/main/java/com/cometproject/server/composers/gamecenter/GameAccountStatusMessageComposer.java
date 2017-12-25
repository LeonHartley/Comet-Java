package com.cometproject.server.composers.gamecenter;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class GameAccountStatusMessageComposer extends MessageComposer {

    private final int gameId;

    public GameAccountStatusMessageComposer(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public short getId() {
        return Composers.GameAccountStatusMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.gameId);
        msg.writeInt(-1);// can play = -1
        msg.writeInt(0);
    }
}
