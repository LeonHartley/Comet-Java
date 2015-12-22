package com.cometproject.server.game.rooms.bundles.types;

import com.cometproject.server.game.rooms.models.CustomFloorMapData;
import com.cometproject.server.game.rooms.models.types.DynamicRoomModelData;

import java.util.List;

public class RoomBundle {
    private int id;
    private int roomId;
    private String alias;
    private CustomFloorMapData roomModelData;
    private List<RoomBundleItem> roomBundleData;
    private RoomBundleConfig config;

    private int costCredits;
    private int costSeasonal;
    private int costVip;

    public RoomBundle(int id, int roomId, String alias, CustomFloorMapData roomModel, List<RoomBundleItem> bundleData, int costCredits, int costSeasonal, int costVip, RoomBundleConfig config) {
        this.id = id;
        this.roomId = roomId;
        this.alias = alias;
        this.roomModelData = roomModel;
        this.roomBundleData = bundleData;
        this.costCredits = costCredits;
        this.costSeasonal = costSeasonal;
        this.costVip = costVip;
        this.config = config;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public CustomFloorMapData getRoomModelData() {
        return roomModelData;
    }

    public void setRoomModelData(CustomFloorMapData roomModelData) {
        this.roomModelData = roomModelData;
    }

    public List<RoomBundleItem> getRoomBundleData() {
        return roomBundleData;
    }

    public void setRoomBundleData(List<RoomBundleItem> roomBundleData) {
        this.roomBundleData = roomBundleData;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getCostCredits() {
        return costCredits;
    }

    public void setCostCredits(int costCredits) {
        this.costCredits = costCredits;
    }

    public int getCostSeasonal() {
        return costSeasonal;
    }

    public void setCostSeasonal(int costSeasonal) {
        this.costSeasonal = costSeasonal;
    }

    public int getCostVip() {
        return costVip;
    }

    public void setCostVip(int costVip) {
        this.costVip = costVip;
    }

    public RoomBundleConfig getConfig() {
        return config;
    }

    public void setConfig(RoomBundleConfig config) {
        this.config = config;
    }
}
