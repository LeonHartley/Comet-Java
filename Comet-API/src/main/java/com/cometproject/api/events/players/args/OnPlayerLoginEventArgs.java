package com.cometproject.api.events.players.args;

import com.cometproject.api.events.EventArgs;
import com.cometproject.api.game.players.BasePlayer;

public class OnPlayerLoginEventArgs extends EventArgs {
    private BasePlayer player;

    public OnPlayerLoginEventArgs(BasePlayer player) {
        this.player = player;
    }

    public BasePlayer getPlayer() {
        return this.player;
    }
}
