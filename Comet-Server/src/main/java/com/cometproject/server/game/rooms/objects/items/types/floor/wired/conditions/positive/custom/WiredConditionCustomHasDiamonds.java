package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.custom;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionCustomHasDiamonds extends WiredConditionItem {
    public static int PARAM_DIAMONDS_ID = 0;

    public WiredConditionCustomHasDiamonds(RoomItemData itemData, Room room) {
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

        final int diamonds = this.getWiredData().getParams().get(PARAM_DIAMONDS_ID);
        boolean hasDiamonds = false;

        PlayerEntity playerEntity = (PlayerEntity) entity;

        if (playerEntity.getPlayer().getData().getVipPoints() >= diamonds) {
            hasDiamonds = true;
        }


        return isNegative != hasDiamonds;
    }
}
