package com.cometproject.server.game.players.components.types.wardrobe;

public class WardrobeClothing {
    private final int id;
    private final int partId;
    private final int part;

    public WardrobeClothing(int id, int partId, int part) {
        this.id = id;
        this.partId = partId;
        this.part = part;
    }

    public int getId() {
        return id;
    }

    public int getPartId() {
        return partId;
    }

    public int getPart() {
        return part;
    }
}
