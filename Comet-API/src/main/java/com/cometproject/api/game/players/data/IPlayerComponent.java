package com.cometproject.api.game.players.data;


import com.cometproject.api.game.players.IPlayer;

public interface IPlayerComponent {
    IPlayer getPlayer();

    void dispose();
}
