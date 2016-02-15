package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionHasFurniOn extends WiredConditionItem {
    private final static int PARAM_MODE = 0;

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
    public WiredConditionHasFurniOn(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public int getInterface() {
        return 7;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        int mode;

        try {
            mode = this.getWiredData().getParams().get(PARAM_MODE);
        } catch (Exception e) {
            mode = 0;
        }

        int selectedItemsWithFurni = 0;

        for (long itemId : this.getWiredData().getSelectedIds()) {
            RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if (floorItem != null) {
                for (RoomItemFloor itemOnSq : floorItem.getItemsOnStack()) {
                    if (itemOnSq.getPosition().getZ() != 0.0 && itemOnSq.getPosition().getZ() > floorItem.getPosition().getZ() && itemOnSq.getId() != floorItem.getId())
                        selectedItemsWithFurni++;
                }
            }
        }

        boolean result = false;

        if(mode == 0) {
            if(selectedItemsWithFurni >= 1) result = true;
        } else {
            if(selectedItemsWithFurni == this.getWiredData().getSelectedIds().size()) result = true;
        }

        return this.isNegative ? !result : result;
    }
}