package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionPlayerHasBadgeEquipped extends WiredConditionItem {

    public WiredConditionPlayerHasBadgeEquipped(RoomItemData itemData, Room room) {        super(itemData, room);    }

    @Override
    public int getInterface() {
        return 11;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        boolean isEquipped = false;
        if (entity == null || !(entity instanceof PlayerEntity)) return false;

        PlayerEntity playerEntity = ((PlayerEntity) entity);

        if (playerEntity.getPlayer().getInventory().hasBadge(this.getWiredData().getText())) {
            int slot = playerEntity.getPlayer().getInventory().getBadges().get(this.getWiredData().getText());

            if (slot != 0)
                isEquipped = true;
        }

        return isNegative != isEquipped;
    }
}
