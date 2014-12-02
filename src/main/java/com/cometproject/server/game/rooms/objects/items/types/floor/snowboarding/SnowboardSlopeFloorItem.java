package com.cometproject.server.game.rooms.objects.items.types.floor.snowboarding;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.items.types.floor.AdjustableHeightFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang.StringUtils;

public class SnowboardSlopeFloorItem extends AdjustableHeightFloorItem {
    public SnowboardSlopeFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }
}
