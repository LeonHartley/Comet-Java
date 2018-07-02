package com.cometproject.api.game.catalog.types.bundles;

import com.cometproject.api.game.rooms.models.CustomFloorMapData;

import java.util.ArrayList;
import java.util.List;

public interface IRoomBundle {
    int getId();

    void setId(int id);

    String getAlias();

    void setAlias(String alias);

    CustomFloorMapData getRoomModelData();

    void setRoomModelData(CustomFloorMapData roomModelData);

    List<RoomBundleItem> getRoomBundleData();

    void setRoomBundleData(List<RoomBundleItem> roomBundleData);

    int getRoomId();

    void setRoomId(int roomId);

    int getCostCredits();

    void setCostCredits(int costCredits);

    int getCostSeasonal();

    void setCostSeasonal(int costSeasonal);

    int getCostVip();

    void setCostVip(int costVip);

    int getCostActivityPoints();

    void setCostActivityPoints(int costActivityPoints);

    RoomBundleConfig getConfig();

    void setConfig(RoomBundleConfig config);
}
