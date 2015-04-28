package com.cometproject.api.game.rooms.entities;

import com.cometproject.api.game.players.IPlayer;

public interface IPlayerEntity extends IRoomEntity {
    IPlayer getPlayer();
}
