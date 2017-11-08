package com.cometproject.api.game.rooms.entities;

import com.cometproject.api.game.players.IPlayer;

public interface PlayerRoomEntity extends RoomEntity {
    IPlayer getPlayer();
}
