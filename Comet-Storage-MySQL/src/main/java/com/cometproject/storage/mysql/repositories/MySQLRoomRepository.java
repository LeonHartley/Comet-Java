package com.cometproject.storage.mysql.repositories;

import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.storage.api.data.rooms.RoomModelData;
import com.cometproject.storage.api.repositories.IRoomItemRepository;
import com.cometproject.storage.api.repositories.IRoomRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.models.factories.rooms.RoomDataFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MySQLRoomRepository extends MySQLRepository implements IRoomRepository {
    private final RoomDataFactory roomDataFactory;

    public MySQLRoomRepository(RoomDataFactory roomDataFactory, MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);

        this.roomDataFactory = roomDataFactory;
    }

    @Override
    public void getAllModels(Consumer<Map<String, RoomModelData>> modelConsumer) {

    }

    @Override
    public void getRoomDataById(int roomId, Consumer<IRoomData> dataConsumer) {

    }

    @Override
    public void getRoomsByPlayerId(int playerId, Consumer<Map<Integer, IRoomData>> dataConsumer) {

    }

    @Override
    public void getRoomsWithRightsByPlayerId(int playerId, Consumer<Map<Integer, IRoomData>> dataConsumer) {

    }

    @Override
    public void getRoomsByQuery(String query, Consumer<List<IRoomData>> dataConsumer) {

    }

    @Override
    public void createRoom(IRoomData data) {

    }

    @Override
    public void updateRoom(IRoomData data) {

    }

    @Override
    public void deleteRoom(int id) {

    }
}
