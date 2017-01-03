package com.cometproject.server.game.rooms.types.components.types;

import java.util.Arrays;

public enum ChatMessageColour {
    RED,
    BLUE,
    GREEN,
    PURPLE,
    CYAN;

    public static String getAllAvailable() {
        return "red, blue, green, purple, cyan";
    }
}
