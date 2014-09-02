package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.items.RoomItemFloor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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
