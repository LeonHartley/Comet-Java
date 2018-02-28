package com.cometproject.storage.mysql.models.factories.rooms;

import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.game.rooms.RoomType;
import com.cometproject.api.game.rooms.settings.*;
import com.cometproject.storage.api.data.rooms.RoomData;

import java.util.List;
import java.util.Map;

public class RoomDataFactory {

    public IRoomData createRoomData(int id, RoomType type, String name, String description, int ownerId, String owner,
                                    int category, int maxUsers, RoomAccessType access, String password,
                                    String originalPassword, RoomTradeState tradeState, int score, String[] tags,
                                    Map<String, String> decorations, String model, boolean hideWalls, int thicknessWall,
                                    int thicknessFloor, boolean allowWalkthrough, boolean allowPets, String heightmap,
                                    RoomMuteState muteState, RoomKickState kickState, RoomBanState banState,
                                    int bubbleMode, int bubbleType, int bubbleScroll, int chatDistance,
                                    int antiFloodSettings, List<String> disabledCommands, int groupId,String requiredBadge,
                                    String thumbnail, boolean wiredHidden) {

        return new RoomData(id, type, name, description, ownerId, owner, category, maxUsers, access, password,
                originalPassword, tradeState, score, tags, decorations, model, hideWalls, thicknessWall, thicknessFloor,
                allowWalkthrough, allowPets, heightmap, muteState, kickState, banState, bubbleMode, bubbleType,
                bubbleScroll, chatDistance, antiFloodSettings, disabledCommands, groupId,
                requiredBadge, thumbnail, wiredHidden);
    }
}
