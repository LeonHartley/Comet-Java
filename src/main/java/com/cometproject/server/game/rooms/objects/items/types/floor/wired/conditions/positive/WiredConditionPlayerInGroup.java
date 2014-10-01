package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;

public class WiredConditionPlayerInGroup extends WiredConditionItem {

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param roomId   The ID of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredConditionPlayerInGroup(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public int getInterface() {
        return 10;
    }

    @Override
    public boolean evaluate(GenericEntity entity, Object data) {
        if(entity == null || !(entity instanceof PlayerEntity)) return false;
        final PlayerEntity playerEntity = ((PlayerEntity) entity);

        final Group group = CometManager.getGroups().getGroupByRoomId(this.getRoom().getId());

        if(group != null) {
            final boolean isMember = playerEntity.getPlayer().getGroups().contains(group.getId());
            return isNegative ? !isMember : isMember;
        }

        return false;
    }
}
