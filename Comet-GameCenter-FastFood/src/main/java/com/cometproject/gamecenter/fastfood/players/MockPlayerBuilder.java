package com.cometproject.gamecenter.fastfood.players;

import com.cometproject.gamecenter.fastfood.net.FastFoodGameSession;

public class MockPlayerBuilder {

    private final FastFoodGameSession gameSession = new FastFoodGameSession();

    public MockPlayerBuilder withUsername(String username) {
        this.gameSession.setUsername(username);
        return this;
    }

    public MockPlayerBuilder withFigure(String figure) {
        this.gameSession.setFigure(figure);
        this.gameSession.setGender("m");

        return this;
    }

    public MockPlayerBuilder withPlayerId(int id) {
        this.gameSession.setPlayerId(id);
        return this;
    }

    public MockPlayer create() {
        return new MockPlayer(this.gameSession);
    }
}
