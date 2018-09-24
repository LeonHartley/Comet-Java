package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionCustomHasRank extends WiredConditionItem {
    public static int PARAM_RANK_ID = 0;

    public WiredConditionCustomHasRank(RoomItemData itemData, Room room) {
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

        final int rankId = this.getWiredData().getParams().get(PARAM_RANK_ID);
        boolean hasRank = false;

        PlayerEntity playerEntity = (PlayerEntity) entity;

        if (playerEntity.getPlayer().getData().getRank() >= rankId) {
            hasRank = true;
        }


        return isNegative != hasRank;
    }
}
