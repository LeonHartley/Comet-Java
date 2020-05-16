package com.cometproject.gamecenter.fastfood.players;

import com.cometproject.gamecenter.fastfood.net.FastFoodGameSession;
import com.cometproject.gamecenter.fastfood.net.FastFoodNetSession;

public class MockPlayer extends FastFoodNetSession {

    public MockPlayer(FastFoodGameSession gameSession) {
        super(null, gameSession, null);
    }
}
