package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionFurniHasPlayers extends WiredConditionItem {
    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredConditionFurniHasPlayers(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
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
            if (itemsWithoutUsersCount == this.getWiredData().getSelectedIds().size()) {
                return true;
            }

            return false;
        } else {
            return itemsWithUserCount == this.getWiredData().getSelectedIds().size();
        }
    }
}
