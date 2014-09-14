package com.cometproject.server.game.rooms.items.types.floor.wired;

import com.cometproject.server.game.rooms.items.RoomItemFloor;

import com.cometproject.server.game.rooms.items.types.floor.wired.actions.WiredActionItem;
import com.cometproject.server.game.rooms.items.types.floor.wired.data.WiredActionItemData;
import com.cometproject.server.game.rooms.items.types.floor.wired.data.WiredItemData;
import com.cometproject.server.network.messages.types.Composer;
import com.google.gson.Gson;

public abstract class AbstractWiredItem extends RoomItemFloor {
    /**
     * GSON instance to share among all wired items
     */
    private static final Gson gson = new Gson();

    /**
     * The data associated with this wired item
     */
    private WiredItemData wiredItemData = null;

    /**
     * The default constructor
     * @param id The ID of the item
     * @param itemId The ID of the item definition
     * @param roomId The ID of the room
     * @param owner The ID of the owner
     * @param x The position of the item on the X axis
     * @param y The position of the item on the Y axis
     * @param z The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data The JSON object associated with this item
     */
    public AbstractWiredItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);

        // TODO: convert old wired data to new wired data

        if(this.getExtraData().isEmpty()) {
            this.setExtraData("{}");
        }

        this.load();
    }

    /**
     * Turn the wired item data into a JSON object, and then save it to the database
     */
    public void save() {
        this.setExtraData(gson.toJson(wiredItemData));
        this.saveData();
    }

    /**
     * Turn the JSON object into a usable WiredItemData object
     */
    public void load() {
        if(this.getExtraData().equals("{}")) {
            this.wiredItemData = (this instanceof WiredActionItem) ? new WiredActionItemData() : new WiredItemData();
        }

        this.wiredItemData = gson.fromJson(this.getExtraData(), (this instanceof WiredActionItem) ? WiredActionItemData.class : WiredItemData.class);
    }

    /**
     * Get the wired item data object
     * @return The wired item data object
     */
    public WiredItemData getWiredData() {
        return wiredItemData;
    }

    public abstract int getInterface();
    public abstract Composer getDialog();
    public abstract boolean evaluate();
}
