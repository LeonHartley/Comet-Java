package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ModToolRoomActionMessageEvent implements IEvent {
    private final static String INAPPROPRIATE_ROOM_NAME = "Inappropriate to hotel management";

    @Override
    public void handle(Session client, Event msg) throws Exception {
        final int roomId = msg.readInt();

        final boolean lockDoor = msg.readInt() == 1;
        final boolean changeRoomName = msg.readInt() == 1;
        final boolean kickAll = msg.readInt() == 1;

        RoomData roomData = CometManager.getRooms().getRoomData(roomId);
        boolean isActive = CometManager.getRooms().isActive(roomId);

        if (roomData == null) return;

        if (lockDoor) {
            roomData.setAccess("doorbell");
        }

        if (changeRoomName) {
            roomData.setName(INAPPROPRIATE_ROOM_NAME);
        }

        if (isActive) {
            Room room = CometManager.getRooms().get(roomData.getId());

            if (room == null) return;

            if (kickAll) {
                
            }


        }
    }
}
