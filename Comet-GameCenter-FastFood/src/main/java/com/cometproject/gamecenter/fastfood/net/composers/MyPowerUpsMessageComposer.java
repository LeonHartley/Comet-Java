package com.cometproject.gamecenter.fastfood.net.composers;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.gamecenter.fastfood.net.FastFoodGameSession;
import com.cometproject.server.protocol.messages.MessageComposer;

public class MyPowerUpsMessageComposer extends MessageComposer {

    private final FastFoodGameSession gameSession;

    public MyPowerUpsMessageComposer(final FastFoodGameSession gameSession) {
        this.gameSession = gameSession;
    }

    @Override
    public short getId() {
        return 14;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(3);

        msg.writeInt(0);
        msg.writeInt(this.gameSession.getParachutes());

        msg.writeInt(1);
        msg.writeInt(this.gameSession.getMissiles());

        msg.writeInt(2);
        msg.writeInt(this.gameSession.getShields());
    }
}
