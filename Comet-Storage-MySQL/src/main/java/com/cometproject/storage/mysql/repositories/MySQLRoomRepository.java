package com.cometproject.storage.mysql.repositories;

import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.game.rooms.models.RoomModelData;
import com.cometproject.storage.api.repositories.IRoomRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.models.factories.rooms.RoomDataFactory;
import com.cometproject.storage.mysql.models.factories.rooms.RoomModelDataFactory;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MySQLRoomRepository extends MySQLRepository implements IRoomRepository {
    private final RoomDataFactory roomDataFactory;
    private final RoomModelDataFactory roomModelDataFactory;

    public MySQLRoomRepository(RoomDataFactory roomDataFactory, RoomModelDataFactory roomModelDataFactory, MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);

        this.roomDataFactory = roomDataFactory;
        this.roomModelDataFactory = roomModelDataFactory;
    }

    @Override
    public void getAllModels(Consumer<Map<String, RoomModelData>> modelConsumer) {
        final Map<String, RoomModelData> roomModels = Maps.newHashMap();

        select("SELECT * FROM room_models", (data) -> {
            final String id = data.readString("id");
            final String heightmap = data.readString("heightmap");
            final int doorX = data.readInteger("door_x");
            final int doorY = data.readInteger("door_y");
            final int doorRotation = data.readInteger("door_dir");

            roomModels.put(id, this.roomModelDataFactory.createData(id, heightmap, doorX, doorY, doorRotation));
        });

        modelConsumer.accept(roomModels);
    }
}
