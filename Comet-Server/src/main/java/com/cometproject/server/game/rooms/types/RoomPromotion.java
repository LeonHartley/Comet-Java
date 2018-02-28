package com.cometproject.server.game.rooms.types;

import com.cometproject.server.boot.Comet;


public class RoomPromotion {
    public static final int DEFAULT_PROMO_LENGTH = 120;

    private int roomId;

    private String promotionName;
    private String promotionDescription;

    private long timestampStart;
    private long timestampFinish;

    public RoomPromotion(int roomId, String name, String description) {
        this.roomId = roomId;
        this.promotionName = name;
        this.promotionDescription = description;

        this.timestampStart = Comet.getTime();
        this.timestampFinish = this.timestampStart + (DEFAULT_PROMO_LENGTH * 60);
    }

    public RoomPromotion(int roomId, String name, String description, long start, long finish) {
        this.roomId = roomId;
        this.promotionName = name;
        this.promotionDescription = description;
        this.timestampStart = start;
        this.timestampFinish = finish;
    }

    public int getRoomId() {
        return this.roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getPromotionName() {
        return this.promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionDescription() {
        return this.promotionDescription;
    }

    public void setPromotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
    }

    public boolean isExpired() {
        return Comet.getTime() >= this.timestampFinish;
    }

    public long getTimestampStart() {
        return this.timestampStart;
    }

    public void setTimestampStart(long timestampStart) {
        this.timestampStart = timestampStart;
    }

    public long getTimestampFinish() {
        return this.timestampFinish;
    }

    public void setTimestampFinish(long timestampFinish) {
        this.timestampFinish = timestampFinish;
    }

    public int minutesLeft() {
        return (int) Math.floor(this.getTimestampFinish() - Comet.getTime()) / 60;
    }
}
