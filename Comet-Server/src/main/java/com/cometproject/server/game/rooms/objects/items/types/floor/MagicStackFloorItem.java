package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

import java.text.DecimalFormat;


public class MagicStackFloorItem extends RoomItemFloor {
    private double magicHeight = 0d;

    public MagicStackFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onPlaced() {
        this.setExtraData("");
        this.magicHeight = 0d;
        this.saveData();
    }

    @Override
    public double getOverrideHeight() {
        return magicHeight;
    }

    public void setOverrideHeight(double magicHeight) {
        this.setExtraData(new DecimalFormat("#.00").format(magicHeight).replace(",", "."));
        this.magicHeight = magicHeight;
    }
}
