package com.cometproject.api.game.players.data;


import com.cometproject.api.game.players.IPlayer;

public interface PlayerComponent {
    IPlayer getPlayer();

    void dispose();
}
