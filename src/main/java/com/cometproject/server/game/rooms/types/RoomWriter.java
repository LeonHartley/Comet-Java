package com.cometproject.server.game.rooms.types;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.types.Composer;

public class RoomWriter {
    public static void write(RoomData room, Composer msg) {
        boolean isActive = CometManager.getRooms().isActive(room.getId());

        msg.writeInt(room.getId());
        msg.writeString(room.getName());
        msg.writeBoolean(true);
        msg.writeInt(room.getOwnerId());
        msg.writeString(room.getOwner());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getAccess()));
        msg.writeInt(!isActive ? 0 : CometManager.getRooms().get(room.getId()).getEntities().playerCount());
        msg.writeInt(room.getMaxUsers());
        msg.writeString(room.getDescription());
        msg.writeInt(0);
        msg.writeInt(room.getCategory().canTrade() ? 2 : 0);
        msg.writeInt(room.getScore());
        msg.writeInt(0);
        msg.writeInt(room.getCategory().getId());
        msg.writeInt(0);
        msg.writeString("");
        msg.writeString("");
        msg.writeString("");
        msg.writeInt(room.getTags().length);

        for (String tag : room.getTags()) {
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

    public static void writeData(RoomData room, Composer msg) {
        boolean isActive = CometManager.getRooms().isActive(room.getId());

        msg.writeBoolean(true);
        msg.writeInt(room.getId());
        msg.writeString(room.getName());
        msg.writeBoolean(true);
        msg.writeInt(room.getOwnerId());
        msg.writeString(room.getOwner());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getAccess()));
        msg.writeInt(!isActive ? 0 : CometManager.getRooms().get(room.getId()).getEntities().playerCount());
        msg.writeInt(room.getMaxUsers());
        msg.writeString(room.getDescription());
        msg.writeInt(room.getCategory().canTrade() ? 2 : 0);
        msg.writeInt(0);
        msg.writeInt(room.getScore());
        msg.writeInt(0);
        msg.writeInt(room.getCategory().getId());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");

        msg.writeInt(room.getTags().length);

        for (String tag : room.getTags()) {
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
        msg.writeBoolean(CometManager.getNavigator().isFeatured(room.getId()));
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

    public static void writeInfo(RoomData room, Composer msg) {
        boolean isActive = CometManager.getRooms().isActive(room.getId());

        msg.writeInt(room.getId());
        msg.writeString(room.getName());
        msg.writeBoolean(true);
        msg.writeInt(room.getOwnerId());
        msg.writeString(room.getOwner());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getAccess()));
        msg.writeInt(isActive ? CometManager.getRooms().get(room.getId()).getEntities().playerCount() : 0);
        msg.writeInt(room.getMaxUsers());
        msg.writeString(room.getDescription());
        msg.writeInt(0);
        msg.writeInt(room.getCategory().canTrade() ? 2 : 0);
        msg.writeInt(room.getScore());
        msg.writeInt(0);
        msg.writeInt(room.getCategory().getId());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");
        msg.writeInt(room.getTags().length);

        for (String tag : room.getTags()) {
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
        } else if(access.equals("invisible")) {
            // return 3; - TODO: this
            return 1;
        }

        return 0;
    }

    public static String roomAccessToString(int access) {
        if (access == 1) {
            return "doorbell";
        } else if (access == 2) {
            return "password";
        } else if(access == 3) {
            // TODO: this
            return "open";
        }

        return "open";
    }
}
