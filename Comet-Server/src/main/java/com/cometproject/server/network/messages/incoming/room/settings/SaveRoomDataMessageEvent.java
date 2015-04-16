package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.game.rooms.types.RoomDataInstance;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.api.game.rooms.settings.RoomBanState;
import com.cometproject.api.game.rooms.settings.RoomKickState;
import com.cometproject.api.game.rooms.settings.RoomMuteState;
import com.cometproject.api.game.rooms.settings.RoomTradeState;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.ConfigureWallAndFloorMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class SaveRoomDataMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (client.getPlayer() == null || client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        RoomInstance room = client.getPlayer().getEntity().getRoom();

        if ((room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        RoomDataInstance data = room.getData();

        int id = msg.readInt();
        String name = msg.readString();
        String description = msg.readString();
        int state = msg.readInt();
        String password = msg.readString();
        int maxUsers = msg.readInt();
        int categoryId = msg.readInt();
        int tagCount = msg.readInt();

        StringBuilder tagBuilder = new StringBuilder();

        for (int i = 0; i < tagCount; i++) {
            if (i > 0) {
                tagBuilder.append(",");
            }

            String tag = msg.readString();
            tagBuilder.append(tag);
        }

        String tagString = tagBuilder.toString();
        String[] tags = tagString.split(",");

        int tradeState = msg.readInt();

        boolean allowPets = msg.readBoolean();
        boolean allowPetsEat = msg.readBoolean();

        boolean allowWalkthrough = msg.readBoolean();
        boolean hideWall = msg.readBoolean();
        int wallThick = msg.readInt();
        int floorThick = msg.readInt();

        int muteState = msg.readInt();
        int kickState = msg.readInt();
        int banState = msg.readInt();

        int bubbleMode = msg.readInt();
        int bubbleType = msg.readInt();
        int bubbleScroll = msg.readInt();
        int chatDistance = msg.readInt();
        int antiFloodSettings = msg.readInt();

        if (wallThick < -2 || wallThick > 1) {
            wallThick = 0;
        }

        if (floorThick < -2 || floorThick > 1) {
            floorThick = 0;
        }

        if (name.length() < 1) {
            return;
        }

        if (state < 0 || state > 3) {
            return;
        }

        if (maxUsers < 0) {
            return;
        }

        /*if (!client.getPlayer().getPermissions().hasPermission("mod_tool") && maxUsers > CometSettings.maxPlayersInRoom) {
            return;
        }*/

        Category category = NavigatorManager.getInstance().getCategory(categoryId);

        if (category == null) {
            return;
        }

        if (category.getRank() > client.getPlayer().getData().getRank()) {
            categoryId = 15; // 15 = the uncategorized category.
        }

        if (tags.length > 2) {
            return;
        }

        data.setAccess(RoomWriter.roomAccessToString(state));
        data.setCategory(categoryId);
        data.setDescription(description);
        data.setName(name);
        data.setPassword(password);
        data.setMaxUsers(maxUsers);
        data.setTags(tags);
        data.setThicknessWall(wallThick);
        data.setThicknessFloor(floorThick);
        data.setHideWalls(hideWall);
        data.setAllowWalkthrough(allowWalkthrough);
        data.setAllowPets(allowPets);

        data.setTradeState(RoomTradeState.valueOf(tradeState));
        data.setMuteState(RoomMuteState.valueOf(muteState));
        data.setKickState(RoomKickState.valueOf(kickState));
        data.setBanState(RoomBanState.valueOf(banState));

        data.setChatDistance(chatDistance);
        data.setBubbleMode(bubbleMode);
        data.setBubbleScroll(bubbleScroll);
        data.setBubbleType(bubbleType);
        data.setAntiFloodSettings(antiFloodSettings);

        try {
            data.save();

            room.getEntities().broadcastMessage(new ConfigureWallAndFloorMessageComposer(hideWall, wallThick, floorThick));
            room.getEntities().broadcastMessage(new RoomDataMessageComposer(room));
        } catch (Exception e) {
            RoomManager.log.error("Error while saving room data", e);
        }
    }
}
