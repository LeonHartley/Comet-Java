package com.cometproject.storage.mysql.models.factories.rooms;

import com.cometproject.api.game.rooms.models.CustomFloorMapData;
import com.cometproject.api.game.rooms.models.RoomModelData;

public class RoomModelDataFactory {

    public  static final RoomModelDataFactory instance = new RoomModelDataFactory();

    public RoomModelData createData(String name, String heightmap, int doorX, int doorY, int doorRotation) {
        return new RoomModelData(name, heightmap, doorX, doorY, doorRotation, -1);
    }

    public RoomModelData createData(CustomFloorMapData customFloorData) {
        return new RoomModelData("dynamic_heightmap", customFloorData.getModelData(), customFloorData.getDoorX(),
                customFloorData.getDoorY(), customFloorData.getDoorRotation(), customFloorData.getWallHeight());
    }

}
