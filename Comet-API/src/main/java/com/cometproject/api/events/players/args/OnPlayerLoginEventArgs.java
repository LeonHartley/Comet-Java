package com.cometproject.api.events.players.args;

import com.cometproject.api.events.EventArgs;
import com.cometproject.api.game.players.IPlayer;

public class OnPlayerLoginEventArgs extends EventArgs {
    private IPlayer player;

    public OnPlayerLoginEventArgs(IPlayer player) {
        this.player = player;
    }

    public IPlayer getPlayer() {
        return this.player;
    }
}
