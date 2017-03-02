package com.cometproject.server.game.items.types;

public enum ItemType {
    WALL("i"),
    FLOOR("s");

    private String type;

    ItemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ItemType forString(final String str) {
        if(str.equals("i")) {
            return WALL;
        }

        return FLOOR;
    }
}
