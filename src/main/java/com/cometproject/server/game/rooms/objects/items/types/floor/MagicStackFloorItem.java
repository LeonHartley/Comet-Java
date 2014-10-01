package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;

import java.text.DecimalFormat;

public class MagicStackFloorItem extends RoomItemFloor {
    private double magicHeight = 0d;

    public MagicStackFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onPlaced() {
        this.setExtraData("");
        this.magicHeight = 0d;
        this.saveData();
    }

    public double getMagicHeight() {
        return magicHeight;
    }

    public void setMagicHeight(double magicHeight) {
        this.setExtraData(new DecimalFormat("#.00").format(magicHeight).replace(",", "."));
        this.magicHeight = magicHeight;
    }
}
