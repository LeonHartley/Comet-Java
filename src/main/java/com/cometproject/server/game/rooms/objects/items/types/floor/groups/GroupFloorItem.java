package com.cometproject.server.game.rooms.objects.items.types.floor.groups;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang.StringUtils;

public class GroupFloorItem extends RoomItemFloor {
    private int groupId;

    public GroupFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if(!StringUtils.isNumeric(data))
            this.groupId = 0;
        else
            this.groupId = Integer.parseInt(data);
    }

    public int getGroupId() {
        return groupId;
    }
}
