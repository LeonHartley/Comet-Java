package com.cometproject.server.game.rooms.types;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.rooms.RoomManager;


public class RoomWriter {
    public static void write(RoomData room, IComposer msg) {
        write(room, msg, false);
    }

    public static void write(RoomData room, IComposer msg, boolean skipAuth) {
        boolean isActive = RoomManager.getInstance().isActive(room.getId());

        msg.writeInt(room.getId());
        msg.writeString(room.getName());
        msg.writeInt(room.getOwnerId());
        msg.writeString(room.getOwner());
        msg.writeInt(skipAuth ? 0 : RoomWriter.roomAccessToNumber(room.getAccess()));
        msg.writeInt(!isActive ? 0 : RoomManager.getInstance().get(room.getId()).getEntities().playerCount());
        msg.writeInt(room.getMaxUsers());
        msg.writeString(room.getDescription());
        msg.writeInt(room.getTradeState().getState());
        msg.writeInt(room.getScore());
        msg.writeInt(0);
        msg.writeInt(room.getCategory().getId());

        msg.writeInt(room.getTags().length);

        for (String tag : room.getTags()) {
            msg.writeString(tag);
        }

        RoomPromotion promotion = RoomManager.getInstance().getRoomPromotions().get(room.getId());
        Group group = GroupManager.getInstance().getGroupByRoomId(room.getId());

        composeRoomSpecials(msg, promotion, group, room.getType());
    }

    public static void entryData(RoomData room, IComposer msg, boolean isLoading, boolean checkEntry, boolean skipAuth) {
        msg.writeBoolean(isLoading); // is loading

        write(room, msg, skipAuth);

        msg.writeBoolean(checkEntry); // check entry??
        msg.writeBoolean(NavigatorManager.getInstance().isFeatured(room.getId()));
        msg.writeBoolean(false); // ??
        msg.writeBoolean(false); // ??

        msg.writeInt(room.getMuteState().getState());
        msg.writeInt(room.getKickState().getState());
        msg.writeInt(room.getBanState().getState());

        msg.writeBoolean(false); // room muting

        msg.writeInt(room.getBubbleMode());
        msg.writeInt(room.getBubbleType());
        msg.writeInt(room.getBubbleScroll());
        msg.writeInt(room.getChatDistance());
        msg.writeInt(room.getAntiFloodSettings());
    }

    public static void composeRoomSpecials(IComposer msg, RoomPromotion promotion, Group group, RoomType roomType) {
        boolean composeGroup = group != null && group.getData() != null;
        boolean composePromo = promotion != null;

        if (composeGroup && composePromo) {
            msg.writeInt(62);
        } else if (composeGroup) {
            msg.writeInt(58);
        } else if (composePromo) {
            msg.writeInt(60);
        } else {
            if (roomType == RoomType.PUBLIC) {
                msg.writeInt(64);
            } else {
                msg.writeInt(56);
            }
        }

        if (composeGroup) {
            composeGroup(group, msg);
        }

        if (composePromo) {
            composePromotion(promotion, msg);
        }
    }

    private static void composePromotion(RoomPromotion promotion, IComposer msg) {
        msg.writeString(promotion.getPromotionName()); // promo name
        msg.writeString(promotion.getPromotionDescription()); // promo description
        msg.writeInt(promotion.minutesLeft()); // promo minutes left
    }

    private static void composeGroup(Group group, IComposer msg) {
        msg.writeInt(group.getId());
        msg.writeString(group.getData().getTitle());
        msg.writeString(group.getData().getBadge());
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
