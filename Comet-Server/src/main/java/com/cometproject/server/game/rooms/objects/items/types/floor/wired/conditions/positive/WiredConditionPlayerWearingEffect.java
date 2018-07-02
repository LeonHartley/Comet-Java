package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionPlayerWearingEffect extends WiredConditionItem {
    public static int PARAM_EFFECT_ID = 0;

    public WiredConditionPlayerWearingEffect(RoomItemData itemData, Room room) {
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

        final int effectId = this.getWiredData().getParams().get(PARAM_EFFECT_ID);
        boolean isWearingEffect = false;


        if (entity.getCurrentEffect() != null) {
            if (entity.getCurrentEffect().getEffectId() == effectId) {
                isWearingEffect = true;
            }
        }

        return isNegative != isWearingEffect;
    }
}
