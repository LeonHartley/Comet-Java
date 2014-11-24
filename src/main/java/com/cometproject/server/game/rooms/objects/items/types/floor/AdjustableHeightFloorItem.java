package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang.StringUtils;

public class AdjustableHeightFloorItem extends SeatFloorItem {
    public AdjustableHeightFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public double getSitHeight() {
        if(!StringUtils.isNumeric(this.getExtraData())) return 1.0;
        return Double.parseDouble(this.getExtraData()) + 1.0;
    }
}
