package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.avatars.effects.UserEffect;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;

public class SummerWaterFloorItem extends RoomItemFloor {
    public SummerWaterFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        entity.applyEffect(new UserEffect(29, true));
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        entity.applyEffect(null);
    }
}
