package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionPlayerHasBadgeEquipped extends WiredConditionItem {
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
    public WiredConditionPlayerHasBadgeEquipped(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

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

        return isNegative ? !isEquipped : isEquipped;
    }
}
