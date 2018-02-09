package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionPlayerInGroup extends WiredConditionItem {

    public WiredConditionPlayerInGroup(RoomItemData itemData, Room room) {
        super(itemData, room);
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
