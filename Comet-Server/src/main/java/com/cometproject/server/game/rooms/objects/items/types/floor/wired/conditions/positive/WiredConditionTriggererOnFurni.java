package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionTriggererOnFurni extends WiredConditionItem {

    public WiredConditionTriggererOnFurni(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (entity == null) return false;

        boolean isOnFurni = false;

        for (long itemId : this.getWiredData().getSelectedIds()) {
            for (RoomItemFloor itemOnEntity : entity.getRoom().getItems().getItemsOnSquare(entity.getPosition().getX(), entity.getPosition().getY())) {
                if (itemOnEntity.getId() == itemId) isOnFurni = true;
            }
        }

        return isNegative != isOnFurni;
    }


    @Override
    public int getInterface() {
        return 8;
    }
}
