package com.cometproject.server.game.rooms.bundles.types;

import com.cometproject.server.game.rooms.models.types.DynamicRoomModelData;

import java.util.List;

public class RoomBundle {
    private int id;
    private int roomId;
    private String alias;
    private DynamicRoomModelData roomModelData;
    private List<RoomBundleItem> roomBundleData;

    public RoomBundle(int id, int roomId, String alias, DynamicRoomModelData roomModel, List<RoomBundleItem> bundleData) {
        this.id = id;
        this.roomId = roomId;
        this.alias = alias;
        this.roomModelData = roomModel;
        this.roomBundleData = bundleData;
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

    public DynamicRoomModelData getRoomModelData() {
        return roomModelData;
    }

    public void setRoomModelData(DynamicRoomModelData roomModelData) {
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
}
