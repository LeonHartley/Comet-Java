package com.cometproject.server.game.rooms.bundles.types;

import com.cometproject.api.game.catalog.types.bundles.IRoomBundle;
import com.cometproject.api.game.catalog.types.bundles.RoomBundleConfig;
import com.cometproject.api.game.catalog.types.bundles.RoomBundleItem;
import com.cometproject.api.game.rooms.models.CustomFloorMapData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.items.types.floor.SoundMachineFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.TeleporterFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
import com.cometproject.server.game.rooms.types.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomBundle implements IRoomBundle {
    private int id;
    private int roomId;
    private String alias;
    private CustomFloorMapData roomModelData;
    private List<RoomBundleItem> roomBundleData;
    private RoomBundleConfig config;

    private int costCredits;
    private int costSeasonal;
    private int costVip;
    private int costActivityPoints;

    public RoomBundle(int id, int roomId, String alias, CustomFloorMapData roomModel, List<RoomBundleItem> bundleData, int costCredits, int costSeasonal, int costVip, int costActivityPoints, RoomBundleConfig config) {
        this.id = id;
        this.roomId = roomId;
        this.alias = alias;
        this.roomModelData = roomModel;
        this.roomBundleData = bundleData;
        this.costCredits = costCredits;
        this.costSeasonal = costSeasonal;
        this.costVip = costVip;
        this.costActivityPoints = costActivityPoints;
        this.config = config;
    }

    public static RoomBundle create(Room room, String alias) {
        CustomFloorMapData modelData = new CustomFloorMapData(
                room.getModel().getDoorX(), room.getModel().getDoorY(),
                room.getModel().getDoorRotation(), room.getModel().getMap(), room.getModel().getRoomModelData().getWallHeight());

        List<RoomBundleItem> bundleItems = new ArrayList<>();

        for (RoomItemFloor floorItem : room.getItems().getFloorItems().values()) {
            if (floorItem instanceof SoundMachineFloorItem || floorItem instanceof TeleporterFloorItem || floorItem instanceof WiredFloorItem) {
                continue;
            }

            bundleItems.add(new RoomBundleItem(floorItem.getItemData().getItemId(),
                    floorItem.getPosition().getX(), floorItem.getPosition().getY(),
                    floorItem.getPosition().getZ(), floorItem.getRotation(), null,
                    floorItem.getDataObject()
            ));
        }

        for (RoomItemWall wallItem : room.getItems().getWallItems().values()) {
            bundleItems.add(new RoomBundleItem(wallItem.getItemData().getItemId(),
                    -1, -1, -1, -1, wallItem.getWallPosition(),
                    wallItem.getItemData().getData()
            ));
        }

        return new RoomBundle(-1, room.getId(), alias, modelData, bundleItems, 20, 0, 0, 0, new RoomBundleConfig("%username%'s new room", room.getData().getDecorationString(), room.getData().getWallThickness(), room.getData().getFloorThickness(), room.getData().getHideWalls()));
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public CustomFloorMapData getRoomModelData() {
        return roomModelData;
    }

    @Override
    public void setRoomModelData(CustomFloorMapData roomModelData) {
        this.roomModelData = roomModelData;
    }

    @Override
    public List<RoomBundleItem> getRoomBundleData() {
        return roomBundleData;
    }

    @Override
    public void setRoomBundleData(List<RoomBundleItem> roomBundleData) {
        this.roomBundleData = roomBundleData;
    }

    @Override
    public int getRoomId() {
        return roomId;
    }

    @Override
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public int getCostCredits() {
        return costCredits;
    }

    @Override
    public void setCostCredits(int costCredits) {
        this.costCredits = costCredits;
    }

    @Override
    public int getCostSeasonal() {
        return costSeasonal;
    }

    @Override
    public void setCostSeasonal(int costSeasonal) {
        this.costSeasonal = costSeasonal;
    }

    @Override
    public int getCostVip() {
        return costVip;
    }

    @Override
    public void setCostVip(int costVip) {
        this.costVip = costVip;
    }

    @Override
    public int getCostActivityPoints() {
        return costActivityPoints;
    }

    @Override
    public void setCostActivityPoints(int costActivityPoints) {
        this.costActivityPoints = costActivityPoints;
    }

    @Override
    public RoomBundleConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(RoomBundleConfig config) {
        this.config = config;
    }
}
