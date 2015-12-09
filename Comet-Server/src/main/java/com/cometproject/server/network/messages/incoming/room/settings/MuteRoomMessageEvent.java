package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.RoomMuteMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class MuteRoomMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer().getEntity() == null)
            return;

        Room room = client.getPlayer().getEntity().getRoom();

        if (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().getRank().roomFullControl()) {
            return;
        }

        if (room.hasRoomMute()) {
            for(GenericEntity entity : room.getEntities().getAllEntities().values()) {
                entity.setRoomMuted(false);
            }

            room.setRoomMute(false);
        } else {
            room.setRoomMute(true);
        }

        room.getEntities().broadcastMessage(new RoomMuteMessageComposer(room.hasRoomMute()));
    }
}
