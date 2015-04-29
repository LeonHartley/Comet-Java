package com.cometproject.api.events.players;

import com.cometproject.api.events.Event;
import com.cometproject.api.game.players.IPlayer;

public class OnPlayerLoginEvent extends Event {

    private IPlayer player;

    public OnPlayerLoginEvent(IPlayer player) {
        this.player = player;
    }

    public IPlayer getPlayer() {
        return player;
    }
}
