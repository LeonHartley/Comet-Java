package com.cometproject.server.game.rooms.types;

import com.cometproject.api.game.rooms.settings.RoomAccessType;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.navigator.types.publics.PublicRoom;
import com.cometproject.server.game.rooms.RoomManager;


public class RoomWriter {
    public static void write(RoomData room, IComposer msg) {
        write(room, msg, false);
    }

    public static void write(RoomData room, IComposer msg, boolean skipAuth) {
        boolean isActive = RoomManager.getInstance().isActive(room.getId());
        PublicRoom publicRoom = NavigatorManager.getInstance().getPublicRoom(room.getId());

        msg.writeInt(room.getId());
        msg.writeString(publicRoom != null ? publicRoom.getCaption() : room.getName());
        msg.writeInt(room.getOwnerId());
        msg.writeString(room.getOwner());
        msg.writeInt(skipAuth ? 0 : RoomWriter.roomAccessToNumber(room.getAccess()));
        msg.writeInt(!isActive ? 0 : RoomManager.getInstance().get(room.getId()).getEntities().playerCount());
        msg.writeInt(room.getMaxUsers());
        msg.writeString(publicRoom != null ? publicRoom.getDescription() : room.getDescription());
        msg.writeInt(room.getTradeState().getState());
        msg.writeInt(room.getScore());
        msg.writeInt(0);
        msg.writeInt(room.getCategory() == null ? 1 : room.getCategory().getId());

        msg.writeInt(room.getTags().length);

        for (String tag : room.getTags()) {
            msg.writeString(tag);
        }

        RoomPromotion promotion = RoomManager.getInstance().getRoomPromotions().get(room.getId());
        Group group = GroupManager.getInstance().get(room.getGroupId());

        composeRoomSpecials(msg, room, promotion, group, room.getType());
    }

    public static void entryData(RoomData room, IComposer msg, boolean isLoading, boolean checkEntry, boolean skipAuth, boolean canMute) {
        msg.writeBoolean(isLoading); // is loading

        write(room, msg, skipAuth);

        msg.writeBoolean(checkEntry); // check entry??
        msg.writeBoolean(NavigatorManager.getInstance().isStaffPicked(room.getId()));
        msg.writeBoolean(false); // ??
        msg.writeBoolean(RoomManager.getInstance().isActive(room.getId()) && RoomManager.getInstance().get(room.getId()).hasRoomMute());

        msg.writeInt(room.getMuteState().getState());
        msg.writeInt(room.getKickState().getState());
        msg.writeInt(room.getBanState().getState());

        msg.writeBoolean(canMute); // room muting

        msg.writeInt(room.getBubbleMode());
        msg.writeInt(room.getBubbleType());
        msg.writeInt(room.getBubbleScroll());
        msg.writeInt(room.getChatDistance());
        msg.writeInt(room.getAntiFloodSettings());
    }

    public static void composeRoomSpecials(IComposer msg, RoomData roomData, RoomPromotion promotion, Group group, RoomType roomType) {
        boolean composeGroup = group != null && group.getData() != null;
        boolean composePromo = promotion != null;

        int specialsType = 0;

        if(group != null)
            specialsType += 2;

        if(promotion != null)
            specialsType += 4;

        if(roomData.isAllowPets()) {
            specialsType += 16;
        }

        PublicRoom publicRoom = NavigatorManager.getInstance().getPublicRoom(roomData.getId());

        if(publicRoom != null)
            specialsType += 1;
        else
            specialsType += 8;

        msg.writeInt(specialsType);

        if(publicRoom != null) {
            msg.writeString(publicRoom.getImageUrl());
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

    public static int roomAccessToNumber(RoomAccessType access) {
        if (access == RoomAccessType.DOORBELL) {
            return 1;
        } else if (access == RoomAccessType.PASSWORD) {
            return 2;
        } else if (access == RoomAccessType.INVISIBLE) {
            // return 3; - TODO: this
            return 1;
        }

        return 0;
    }

    public static RoomAccessType roomAccessToString(int access) {
        if (access == 1) {
            return RoomAccessType.DOORBELL;
        } else if (access == 2) {
            return RoomAccessType.PASSWORD;
        } else if (access == 3) {
            // TODO: this (invisible)
            return RoomAccessType.OPEN;
        }

        return RoomAccessType.OPEN;
    }
}
