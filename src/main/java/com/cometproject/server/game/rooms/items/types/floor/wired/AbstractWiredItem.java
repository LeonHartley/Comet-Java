package com.cometproject.server.game.rooms.items.types.floor.wired;

import com.cometproject.server.game.rooms.items.RoomItemFloor;

import java.util.List;

public abstract class AbstractWiredItem extends RoomItemFloor {
    public static final int MAX_SELECTION = 5;
    public static final int PARAM_STATE = 0;
    public static final int PARAM_ROTATION = 1;
    public static final int PARAM_POSITION = 2;

    private int selectionType;
    private List<Integer> selectedIds;
    private String text;
    private int[] params;

    public AbstractWiredItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }
}
