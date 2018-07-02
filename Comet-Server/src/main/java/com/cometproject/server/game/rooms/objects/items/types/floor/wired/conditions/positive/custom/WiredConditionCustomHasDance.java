package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionCustomHasDance extends WiredConditionItem {
    public static int PARAM_DANCE_ID = 0;

    public WiredConditionCustomHasDance(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getInterface() {
        return 12;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (entity == null) return false;

        if (this.getWiredData().getParams().size() != 1) {
            return false;
        }

        final int danceId = this.getWiredData().getParams().get(PARAM_DANCE_ID);
        boolean snapDancing = false;

        PlayerEntity playerEntity = (PlayerEntity) entity;

        if (playerEntity.getDanceId() == danceId) {
            snapDancing = true;
        }


        return isNegative != snapDancing;
    }
}
