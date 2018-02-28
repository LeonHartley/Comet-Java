package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

import java.text.DecimalFormat;


public class MagicStackFloorItem extends RoomItemFloor {
    private double magicHeight = 0d;

    public MagicStackFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onPlaced() {
        this.getItemData().setData("");
        this.magicHeight = 0d;
        this.saveData();
    }

    @Override
    public double getOverrideHeight() {
        return magicHeight;
    }

    public void setOverrideHeight(double magicHeight) {
        this.getItemData().setData(new DecimalFormat("#.00").format(magicHeight).replace(",", "."));
        this.magicHeight = magicHeight;
    }
}
