package com.cometproject.gamecenter.fastfood.players;

import com.cometproject.gamecenter.fastfood.net.FastFoodGameSession;
import com.cometproject.gamecenter.fastfood.net.FastFoodNetSession;
import com.cometproject.networking.api.messages.IMessageHandler;
import io.netty.channel.ChannelHandlerContext;

public class MockPlayer extends FastFoodNetSession {

    public MockPlayer(FastFoodGameSession gameSession) {
        super(null, gameSession, null);
    }
}
