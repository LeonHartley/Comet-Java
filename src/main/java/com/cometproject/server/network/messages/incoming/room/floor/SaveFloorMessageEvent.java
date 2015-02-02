package com.cometproject.server.network.messages.incoming.room.floor;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.models.CustomFloorMapData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.JsonFactory;


public class SaveFloorMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        String model = msg.readString();
        final int doorX = msg.readInt();
        final int doorY = msg.readInt();
        final int doorRotation = msg.readInt();
        final int wallThickness = msg.readInt();
        final int floorThickness = msg.readInt();
        final int wallHeight = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        if ((room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        model = model.replace((char) 10, (char) 0);
        String[] modelData = model.split(String.valueOf((char) 13));

        int sizeY = modelData.length;
        int sizeX = modelData[0].length();

        if (sizeX < 2 || sizeY < 2 || sizeX > CometSettings.floorMaxX || sizeY > CometSettings.floorMaxY || CometSettings.floorMaxTotal < (sizeX * sizeY)) {
            client.send(AdvancedAlertMessageComposer.compose("Invalid Model", Locale.get("command.floor.size")));
            return;
        }

        boolean hasTiles = false;
        boolean validDoor = false;

        for (int y = 0; y < sizeY; y++) {
            String modelLine = modelData[y];

            for (int x = 0; x < sizeX; x++) {
                if(x < (modelLine.length() + 1) ) {
                    if (!Character.toString(modelLine.charAt(x)).equals("x")) {
                        if (x == doorX && y == doorY) {
                            validDoor = true;
                        }

                        hasTiles = true;
                    }
                }
            }
        }

        if (!hasTiles || !validDoor) {
            client.send(AdvancedAlertMessageComposer.compose("Invalid Model", Locale.get("command.floor.invalid")));
            return;
        }

        room.getData().setThicknessWall(wallThickness);
        room.getData().setThicknessFloor(floorThickness);

        final CustomFloorMapData floorMapData = new CustomFloorMapData(doorX, doorY, doorRotation, model.trim(), wallHeight);

        room.getData().setHeightmap(JsonFactory.getInstance().toJson(floorMapData));
        room.getData().save();

        client.send(AdvancedAlertMessageComposer.compose("Model Saved", Locale.get("command.floor.complete"), "Go", "event:navigator/goto/" + client.getPlayer().getEntity().getRoom().getId(), ""));

        room.setIdleNow();
    }
}
