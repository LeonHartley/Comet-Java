package com.cometproject.server.game.rooms.types;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.types.Composer;


public class RoomWriter {
    public static void write(RoomData room, Composer msg) {
        boolean isActive = RoomManager.getInstance().isActive(room.getId());

        msg.writeInt(room.getId());
        msg.writeString(room.getName());
        msg.writeBoolean(true);
        msg.writeInt(room.getOwnerId());
        msg.writeString(room.getOwner());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getAccess()));
        msg.writeInt(!isActive ? 0 : RoomManager.getInstance().get(room.getId()).getEntities().playerCount());
        msg.writeInt(room.getMaxUsers());
        msg.writeString(room.getDescription());
        msg.writeInt(0);
        msg.writeInt(room.getScore());
        msg.writeInt(room.getCategory().canTrade() ? 2 : 0);
        msg.writeInt(0);
        msg.writeInt(room.getCategory().getId());

        composeGroup(msg, room);

        msg.writeString("");
        msg.writeInt(room.getTags().length);

        for (String tag : room.getTags()) {
            msg.writeString(tag);
        }

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(false);
        msg.writeBoolean(false);

        RoomPromotion promotion = RoomManager.getInstance().getRoomPromotions().get(room.getId());

        msg.writeInt(promotion != null ? 1 : 0); // has promo
        msg.writeString(promotion != null ? promotion.getPromotionName() : ""); // promo name
        msg.writeString(promotion != null ? promotion.getPromotionDescription() : ""); // promo description
        msg.writeInt(promotion != null ? promotion.minutesLeft() : 0); // promo minutes left
    }

    public static void writeData(RoomData room, Composer msg) {
        boolean isActive = RoomManager.getInstance().isActive(room.getId());

        msg.writeBoolean(true);
        msg.writeInt(room.getId());
        msg.writeString(room.getName());
        msg.writeBoolean(true);
        msg.writeInt(room.getOwnerId());
        msg.writeString(room.getOwner());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getAccess()));
        msg.writeInt(!isActive ? 0 : RoomManager.getInstance().get(room.getId()).getEntities().playerCount());
        msg.writeInt(room.getMaxUsers());
        msg.writeString(room.getDescription());
        msg.writeInt(room.getTradeState().getState());
        msg.writeInt(room.getScore());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(room.getCategory().getId());

        composeGroup(msg, room);

        msg.writeString("");

        msg.writeInt(room.getTags().length);

        for (String tag : room.getTags()) {
            msg.writeString(tag);
        }

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(room.isAllowPets()); //allowpets
        msg.writeBoolean(true); // room enter ad :s
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(false);
        msg.writeBoolean(NavigatorManager.getInstance().isFeatured(room.getId()));
        msg.writeBoolean(false);

        msg.writeBoolean(false); // IS_MUTED
        msg.writeInt(room.getMuteState().getState());
        msg.writeInt(room.getKickState().getState());
        msg.writeInt(room.getBanState().getState());
        msg.writeBoolean(false);

        msg.writeInt(room.getBubbleMode());
        msg.writeInt(room.getBubbleType());
        msg.writeInt(room.getBubbleScroll());
        msg.writeInt(room.getChatDistance());
        msg.writeInt(room.getAntiFloodSettings());
//        msg.writeBoolean(false);
//        msg.writeBoolean(false);
//        msg.writeBoolean(true);
    }

    public static void writeInfo(RoomData room, Composer msg) {
        boolean isActive = RoomManager.getInstance().isActive(room.getId());

        msg.writeInt(room.getId());
        msg.writeString(room.getName());
        msg.writeBoolean(true);
        msg.writeInt(room.getOwnerId());
        msg.writeString(room.getOwner());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getAccess()));
        msg.writeInt(isActive ? RoomManager.getInstance().get(room.getId()).getEntities().playerCount() : 0);
        msg.writeInt(room.getMaxUsers());
        msg.writeString(room.getDescription());
        msg.writeInt(0);
        msg.writeInt(room.getCategory().canTrade() ? 2 : 0);
        msg.writeInt(room.getScore());
        msg.writeInt(0);
        msg.writeInt(room.getCategory().getId());

        composeGroup(msg, room);

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
        } else if (access.equals("invisible")) {
            // return 3; - TODO: this
            return 1;
        }

        return 0;
    }

    private static void composeGroup(Composer msg, RoomData roomData) {
        if (GroupManager.getInstance().getGroupByRoomId(roomData.getId()) == null) {
            msg.writeInt(0);
            msg.writeInt(0);
        } else {
            Group group = GroupManager.getInstance().getGroupByRoomId(roomData.getId());

            if (group == null) {
                msg.writeInt(0);
                msg.writeInt(0);
            } else {
                msg.writeInt(group.getId());
                msg.writeString(group.getData().getTitle());
                msg.writeString(group.getData().getBadge());
            }
        }
    }

    public static String roomAccessToString(int access) {
        if (access == 1) {
            return "doorbell";
        } else if (access == 2) {
            return "password";
        } else if (access == 3) {
            // TODO: this
            return "open";
        }

        return "open";
    }
}
