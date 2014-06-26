package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.ConfigureWallAndFloorMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class SaveRoomDataMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        RoomData data = room.getData();

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

        int junk = msg.readInt();

        boolean allowPets = msg.readBoolean();
        boolean allowPetsEat = msg.readBoolean();

        boolean allowWalkthrough = msg.readBoolean();
        boolean hideWall = msg.readBoolean();
        int wallThick = msg.readInt();
        int floorThick = msg.readInt();

        int whoMute = msg.readInt();
        int whoKick = msg.readInt();
        int whoBan = msg.readInt();

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

        Category category = CometManager.getNavigator().getCategory(categoryId);

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

        try {
            data.save();

            room.getEntities().broadcastMessage(ConfigureWallAndFloorMessageComposer.compose(hideWall, wallThick, floorThick));
            room.getEntities().broadcastMessage(RoomDataMessageComposer.compose(room));
        } catch (Exception e) {
            RoomManager.log.error("Error while saving room data", e);
        }
    }
}
