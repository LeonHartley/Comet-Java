package com.cometproject.server.game.rooms.types;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.game.rooms.RoomType;
import com.cometproject.api.game.rooms.settings.RoomAccessType;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.navigator.types.publics.PublicRoom;
import com.cometproject.server.game.rooms.RoomManager;


public class RoomWriter {
    public static void write(IRoomData room, IComposer msg) {
        write(room, msg, false);
    }

    public static void write(IRoomData room, IComposer msg, boolean skipAuth) {
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
        msg.writeInt(room.getCategoryId());

        msg.writeInt(room.getTags().length);

        for (String tag : room.getTags()) {
            msg.writeString(tag);
        }

        RoomPromotion promotion = RoomManager.getInstance().getRoomPromotions().get(room.getId());
        IGroupData group = GameContext.getCurrent().getGroupService().getData(room.getGroupId());

        composeRoomSpecials(msg, room, promotion, group, room.getType());
    }

    public static void entryData(IRoomData room, IComposer msg, boolean isLoading, boolean checkEntry, boolean skipAuth, boolean canMute) {
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

    public static void composeRoomSpecials(IComposer msg, IRoomData roomData, RoomPromotion promotion, IGroupData group, RoomType roomType) {
        boolean composeGroup = group != null;
        boolean composePromo = promotion != null;

        int specialsType = 0;

        // Group - Promotion - AllowPets - PublicRoom - Thumbnail - Value
        //   0         0            0           1           0         0
        //   0         0            0           1           1         1
        //   1         0            0           1           0         2
        //   1         0            0           1           1         3
        //   0         1            0           1           0         4
        //   0         1            0           1           1         5
        //   1         1            0           1           0         6
        //   1         1            0           1           1         7
        //   0         0            0           0           0         8
        //   0         0            0           0           1         9
        //   1         0            0           0           0         10
        //   1         0            0           0           1         11
        //   0         1            0           0           0         12
        //   0         1            0           0           1         13
        //   1         1            0           0           0         14
        //   1         1            0           0           1         15
        //   0         0            1           1           0         16
        //   0         0            1           1           1         17
        //   1         0            1           1           0         18
        //   1         0            1           1           1         19
        //   0         1            1           1           0         20
        //   0         1            1           1           1         21
        //   1         1            1           1           0         22
        //   1         1            1           1           1         23
        //   0         0            1           0           0         24
        //   0         0            1           0           1         25
        //   1         0            1           0           0         26
        //   1         0            1           0           1         27
        //   0         1            1           0           0         28
        //   0         1            1           0           1         29
        //   1         1            1           0           0         30
        //   1         1            1           0           1         31

        if (group != null)
            specialsType += 2;

        if (promotion != null)
            specialsType += 4;

        if (roomData.isAllowPets()) {
            specialsType += 16;
        }

        PublicRoom publicRoom = NavigatorManager.getInstance().getPublicRoom(roomData.getId());
        final boolean thumbnail = roomData.getThumbnail() != null && !roomData.getThumbnail().isEmpty();

        if (publicRoom == null)
            specialsType += 8;

        msg.writeInt(specialsType + (thumbnail ? 1 : 0));

        if (publicRoom != null) {
            msg.writeString(publicRoom.getImageUrl());

        } else {
            if (roomData.getThumbnail() != null && !roomData.getThumbnail().isEmpty()) {
                msg.writeString(roomData.getThumbnail());
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

    private static void composeGroup(IGroupData group, IComposer msg) {
        msg.writeInt(group.getId());
        msg.writeString(group.getTitle());
        msg.writeString(group.getBadge());
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
