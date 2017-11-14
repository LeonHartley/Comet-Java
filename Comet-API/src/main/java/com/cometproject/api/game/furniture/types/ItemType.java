package com.cometproject.api.game.furniture.types;

public enum ItemType {
    WALL("i"),
    FLOOR("s"),
    EFFECT("e");

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
        } else if(str.equals("e")) {
            return EFFECT;
        }

        return FLOOR;
    }
}
