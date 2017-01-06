package com.cometproject.server.game.rooms.types.components.types;

import java.util.Arrays;

public enum ChatMessageColour {
    red,
    blue,
    green,
    purple,
    cyan;

    public static String getAllAvailable() {
        return "red, blue, green, purple, cyan, normal";
    }
}
