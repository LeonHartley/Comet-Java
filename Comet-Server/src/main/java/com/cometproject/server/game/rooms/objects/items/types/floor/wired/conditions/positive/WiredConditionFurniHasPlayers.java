package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionFurniHasPlayers extends WiredConditionItem {

    public WiredConditionFurniHasPlayers(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getInterface() {
        return 8;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        int itemsWithUserCount = 0;
        int itemsWithoutUsersCount = 0;

        for (long itemId : this.getWiredData().getSelectedIds()) {
            RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if (floorItem != null) {
                if (floorItem.getEntitiesOnItem().size() != 0) {
                    itemsWithUserCount++;
                } else {
                    itemsWithoutUsersCount++;
                }
            }
        }

        if (isNegative) {
            return itemsWithoutUsersCount == this.getWiredData().getSelectedIds().size();

        } else {
            return itemsWithUserCount == this.getWiredData().getSelectedIds().size();
        }
    }
}
