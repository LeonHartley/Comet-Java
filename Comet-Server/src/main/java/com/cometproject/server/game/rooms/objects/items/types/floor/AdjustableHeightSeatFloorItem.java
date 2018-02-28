package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang.StringUtils;


public class AdjustableHeightSeatFloorItem extends SeatFloorItem {
    public AdjustableHeightSeatFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);

        if (this.getItemData().getData().isEmpty()) {
            this.getItemData().setData("0");
        }
    }

    @Override
    public double getSitHeight() {
        double height;

        if (!StringUtils.isNumeric(this.getItemData().getData())) {
            height = 1.0;
        } else {
            height = Double.parseDouble(this.getItemData().getData());

            if (height <= 1) {
                height += 1.0;
            } else {
                height += 0.5;
            }
        }

        return height;
    }
}
