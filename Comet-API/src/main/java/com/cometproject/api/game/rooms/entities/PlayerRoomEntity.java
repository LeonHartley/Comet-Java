package com.cometproject.api.game.rooms.entities;

import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.rooms.IRoom;

public interface PlayerRoomEntity extends RoomEntity {
    IPlayer getPlayer();

    IRoom getRoom();
}
