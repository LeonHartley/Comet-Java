package com.cometproject.storage.api.repositories;

import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.game.rooms.models.RoomModelData;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface IRoomRepository {

    void getAllModels(Consumer<Map<String, RoomModelData>> modelConsumer);

    void getRoomDataById(int roomId, Consumer<IRoomData> dataConsumer);
//
//    void getRoomsByPlayerId(int playerId, Consumer<Map<Integer, IRoomData>> dataConsumer);
//
//    void getRoomsWithRightsByPlayerId(int playerId, Consumer<Map<Integer, IRoomData>> dataConsumer);
//
//    void getRoomsByQuery(String query, Consumer<List<IRoomData>> dataConsumer);
//
//    void createRoom(IRoomData data);
//
    void updateRoom(IRoomData data);
//
//    void deleteRoom(int id);


}
