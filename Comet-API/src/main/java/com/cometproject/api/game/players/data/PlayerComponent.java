package com.cometproject.api.game.players.data;


import com.cometproject.api.game.players.BasePlayer;

public interface PlayerComponent {
    BasePlayer getPlayer();

    void dispose();
}
