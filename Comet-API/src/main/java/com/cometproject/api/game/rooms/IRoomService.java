package com.cometproject.api.game.rooms;

import com.cometproject.api.utilities.Initialisable;

public interface IRoomService extends Initialisable {
    IRoomData getRoomData(int roomId);
}
