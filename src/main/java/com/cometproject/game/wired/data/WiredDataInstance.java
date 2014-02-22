package com.cometproject.game.wired.data;

public abstract class WiredDataInstance {
    private int id;
    private int itemId;

    private String type;

    public WiredDataInstance(String type, int id, int itemId) {
        this.id = id;
        this.itemId = itemId;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public int getItemId() {
        return this.itemId;
    }

    public String getType() {
        return this.type;
    }
}
