package com.cometproject.game.rooms.services;

import com.cometproject.api.caching.Cache;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.game.rooms.IRoomService;
import com.cometproject.game.rooms.models.RoomModel;
import com.cometproject.storage.api.data.Data;
import com.cometproject.storage.api.repositories.IRoomRepository;

public class RoomService implements IRoomService {

    private final IRoomRepository roomRepository;
    private final Cache<Integer, IRoomData> roomDataCache;

    public RoomService(final IRoomRepository roomRepository, final Cache<Integer, IRoomData> roomDataCache) {
        this.roomRepository = roomRepository;
        this.roomDataCache = roomDataCache;
    }

    @Override
    public IRoomData getRoomData(int roomId) {
        if(roomId == 0) {
            return null;
        }

        Data<IRoomData> roomData = Data.createEmpty();

        if (this.roomDataCache.contains(roomId)) {
            return this.roomDataCache.get(roomId);
        }

        this.roomRepository.getRoomDataById(roomId, roomData::set);

        if(roomData.has()) {
            this.roomDataCache.add(roomId, roomData.get());
            return roomData.get();
        }

        return null;
    }

    @Override
    public void saveRoomData(IRoomData roomData) {
        this.roomRepository.updateRoom(roomData);
    }
}
