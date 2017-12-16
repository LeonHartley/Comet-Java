package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionPlayerInGroup extends WiredConditionItem {

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
    public WiredConditionPlayerInGroup(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public int getInterface() {
        return 10;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (entity == null || !(entity instanceof PlayerEntity)) return false;
        final PlayerEntity playerEntity = ((PlayerEntity) entity);

        final IGroup group = this.getRoom().getGroup();

        if (group != null) {
            final boolean isMember = playerEntity.getPlayer().getGroups().contains(group.getId());
            return isNegative != isMember;
        }

        return false;
    }
}
