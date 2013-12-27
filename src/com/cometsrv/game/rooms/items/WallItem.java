package com.cometsrv.game.rooms.items;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.items.types.ItemDefinition;
import com.cometsrv.network.messages.types.Composer;

public class WallItem {
    private int id;
    private int itemId;
    private int owner;
    private String position;
    private String extraData;
    private boolean state;

    public WallItem(int id, int itemId, int owner, String position, String data) {
        this.id = id;
        this.itemId = itemId;
        this.owner = owner;
        this.position = position;
        this.extraData = data;

        state = false;
    }

    public void serialize(Composer msg) {
        msg.writeString(this.getId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeString(this.position);
        GameEngine.getLogger().info("Position => "+ this.position);
        msg.writeString(this.getExtraData());
        msg.writeInt(!this.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(-1);
        msg.writeInt(-1);
    }

    public ItemDefinition getDefinition() {
        return GameEngine.getItems().getDefintion(this.getItemId());
    }

    public int getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
    }

    public int getOwner() {
        return owner;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return this.position;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String data) {
        this.extraData = data;
    }

    public boolean getState() {
        return this.state;
    }

}
