package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class ModToolRoomActionMessageEvent implements IEvent {
    private final static String INAPPROPRIATE_ROOM_NAME = "Inappropriate to hotel management";

    @Override
    public void handle(Session client, Event msg) throws Exception {
        final int roomId = msg.readInt();

        if (!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.disconnect();
            return;
        }

        final boolean lockDoor = msg.readInt() == 1;
        final boolean changeRoomName = msg.readInt() == 1;
        final boolean kickAll = msg.readInt() == 1;

        RoomData roomData = RoomManager.getInstance().getRoomData(roomId);
        boolean isActive = RoomManager.getInstance().isActive(roomId);

        if (roomData == null) return;

        if (lockDoor) {
            roomData.setAccess("doorbell");
        }

        if (changeRoomName) {
            roomData.setName(Locale.getOrDefault("game.room.inappropriate", INAPPROPRIATE_ROOM_NAME));
        }

        if (isActive) {
            Room room = RoomManager.getInstance().get(roomData.getId());

            if (room == null) return;

            if (kickAll) {
                for (PlayerEntity entity : room.getEntities().getPlayerEntities()) {
                    if (entity.getPlayer().getPermissions().hasPermission("room_unkickable")) continue;

                    entity.kick();
                }
            }

            if (lockDoor || changeRoomName && !kickAll) {
                room.getEntities().broadcastMessage(new RoomDataMessageComposer(room));
            }
        }

        roomData.save();
    }
}
