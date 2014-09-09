package com.cometproject.server.game.rooms.items.types.floor.wired;

import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.network.messages.types.Composer;
import com.google.gson.Gson;

import java.util.List;

public abstract class AbstractWiredItem extends RoomItemFloor {
    public static final Gson gsonInstance = new Gson();

    public static final int MAX_SELECTION = 5;
    public static final int PARAM_STATE = 0;
    public static final int PARAM_ROTATION = 1;
    public static final int PARAM_POSITION = 2;

    private int selectionType;
    private List<Integer> selectedIds;
    private String text;
    private int[] params;
    private List<WiredItemSnapshot> snapshots;

    public AbstractWiredItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);

        if(this.getExtraData().isEmpty()) {
            this.setExtraData("{}");
        }

        this.load();
    }

    public void save() {
        // turn all the data associated with the wired item into a JSON object.
    }

    public void load() {
        if(this.getExtraData().equals("{}")) {
            // No data available, what do?
        }

        // Turn the JSON object into all data for the wired item
    }

    public abstract int getInterface();
    public abstract Composer getDialog();
    public abstract boolean evaluate();
}
