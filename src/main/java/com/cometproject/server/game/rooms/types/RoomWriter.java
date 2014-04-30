package com.cometproject.server.game.rooms.types;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.types.Composer;

public class RoomWriter {
    public static void write(Room room, Composer msg) {
        msg.writeInt(room.getId());
        msg.writeString(room.getData().getName());
        msg.writeBoolean(true);
        msg.writeInt(room.getData().getOwnerId());
        msg.writeString(room.getData().getOwner());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getData().getAccess()));
        msg.writeInt(room.getEntities() == null ? 0 : room.getEntities().playerCount());
        msg.writeInt(room.getData().getMaxUsers());
        msg.writeString(room.getData().getDescription());
        msg.writeInt(0);
        msg.writeInt(room.getData().getCategory().canTrade() ? 2 : 0);
        msg.writeInt(room.getData().getScore());
        msg.writeInt(0);
        msg.writeInt(room.getData().getCategory().getId());
        msg.writeInt(0);
        msg.writeString("");
        msg.writeString("");
        msg.writeString("");
        msg.writeInt(room.getData().getTags().length);

        for (String tag : room.getData().getTags()) {
            msg.writeString(tag);
        }

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeInt(0); // has promo
        msg.writeString(""); // promo name
        msg.writeString(""); // promo description
        msg.writeInt(0); // promo minutes left
    }

    public static void writeData(Room room, Composer msg) {
        msg.writeBoolean(true);
        msg.writeInt(room.getId());
        msg.writeString(room.getData().getName());
        msg.writeBoolean(true);
        msg.writeInt(room.getData().getOwnerId());
        msg.writeString(room.getData().getOwner());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getData().getAccess()));
        msg.writeInt(room.getEntities().playerCount());
        msg.writeInt(room.getData().getMaxUsers());
        msg.writeString(room.getData().getDescription());
        msg.writeInt(room.getData().getCategory().canTrade() ? 2 : 0);
        msg.writeInt(0);
        msg.writeInt(room.getData().getScore());
        msg.writeInt(0);
        msg.writeInt(room.getData().getCategory().getId());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");

        msg.writeInt(room.getData().getTags().length);

        for (String tag : room.getData().getTags()) {
            msg.writeString(tag);
        }

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(false);
        msg.writeBoolean(GameEngine.getNavigator().isFeatured(room.getId()));
        msg.writeBoolean(false);

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(2);
        msg.writeInt(0);
        msg.writeInt(14);
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeBoolean(true);

    }

    public static void writeInfo(Room room, Composer msg) {
        msg.writeInt(room.getId());
        msg.writeString(room.getData().getName());
        msg.writeBoolean(true);
        msg.writeInt(room.getData().getOwnerId());
        msg.writeString(room.getData().getOwner());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getData().getAccess()));
        msg.writeInt(room.getEntities() != null ? room.getEntities().playerCount() : 0);
        msg.writeInt(room.getData().getMaxUsers());
        msg.writeString(room.getData().getDescription());
        msg.writeInt(0);
        msg.writeInt(room.getData().getCategory().canTrade() ? 2 : 0);
        msg.writeInt(room.getData().getScore());
        msg.writeInt(0);
        msg.writeInt(room.getData().getCategory().getId());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");
        msg.writeInt(room.getData().getTags().length);

        for (String tag : room.getData().getTags()) {
            msg.writeString(tag);
        }

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(0);
    }

    public static int roomAccessToNumber(String access) {
        if (access.equals("doorbell")) {
            return 1;
        } else if (access.equals("password")) {
            return 2;
        }

        return 0;
    }

    public static String roomAccessToString(int access) {
        if (access == 1) {
            return "doorbell";
        } else if (access == 2) {
            return "password";
        }

        return "open";
    }
}
