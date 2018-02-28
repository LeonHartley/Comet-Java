package com.cometproject.api.game.rooms;

import com.cometproject.api.utilities.Initialisable;

public interface IRoomService {
    IRoomData getRoomData(int roomId);

    void saveRoomData(IRoomData roomData);
}
