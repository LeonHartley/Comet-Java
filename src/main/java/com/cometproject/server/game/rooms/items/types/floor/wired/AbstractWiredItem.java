package com.cometproject.server.game.rooms.items.types.floor.wired;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;

import com.cometproject.server.game.rooms.items.types.floor.wired.actions.WiredActionItem;
import com.cometproject.server.game.rooms.items.types.floor.wired.data.WiredActionItemData;
import com.cometproject.server.game.rooms.items.types.floor.wired.data.WiredItemData;
import com.cometproject.server.network.messages.types.Composer;
import com.google.gson.Gson;

import java.util.StringTokenizer;
import java.util.concurrent.Callable;

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
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param roomId   The ID of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public AbstractWiredItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);

        // TODO: convert old wired data to new wired data

        if (!this.getExtraData().startsWith("{")) {
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
        if (this.getExtraData().equals("{}")) {
            this.wiredItemData = (this instanceof WiredActionItem) ? new WiredActionItemData() : new WiredItemData();
        }

        this.wiredItemData = gson.fromJson(this.getExtraData(), (this instanceof WiredActionItem) ? WiredActionItemData.class : WiredItemData.class);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity p = (PlayerEntity) entity;

        if (!this.getRoom().getRights().hasRights(p.getPlayerId()) && !p.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        ((PlayerEntity) entity).getPlayer().getSession().send(this.getDialog());
    }

    /**
     * Get the wired item data object
     *
     * @return The wired item data object
     */
    public WiredItemData getWiredData() {
        return wiredItemData;
    }

    /**
     * Get the ID of the interface of the item which will tell the client which input fields to display
     *
     * @return The ID of the interface
     */
    public abstract int getInterface();

    /**
     * Get the packet to display the full dialog to the player
     *
     * @return The dialog message composer
     */
    public abstract Composer getDialog();

    /**
     * Execute the wired item's
     *
     * @return whether or not the evaluation was a success
     */
    public abstract boolean evaluate();
}
