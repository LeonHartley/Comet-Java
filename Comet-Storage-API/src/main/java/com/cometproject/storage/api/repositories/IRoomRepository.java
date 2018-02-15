package com.cometproject.storage.api.repositories;

import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.storage.api.data.rooms.RoomModelData;

import java.util.Map;
import java.util.function.Consumer;

public interface IRoomRepository {

    void getAllModels(Consumer<Map<String, RoomModelData>> modelConsumer);

    void getRoomDataById(int roomId, Consumer<IRoomData> dataConsumer);

    

}
