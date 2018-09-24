package com.cometproject.server.game.players.types;

import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.players.data.IPlayerComponent;

public abstract class PlayerComponent implements IPlayerComponent {
    private final IPlayer player;

    public PlayerComponent(IPlayer player) {
        this.player = player;
    }

    public void dispose() {
    }

    public IPlayer getPlayer() {
        return this.player;
    }
}
