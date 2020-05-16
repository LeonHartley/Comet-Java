package com.cometproject.api.game.rooms;

public interface IRoomService {
    IRoomData getRoomData(int roomId);

    void saveRoomData(IRoomData roomData);
}
