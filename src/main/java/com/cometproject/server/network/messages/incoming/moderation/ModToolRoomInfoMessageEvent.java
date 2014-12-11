package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolRoomInfoMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class ModToolRoomInfoMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int roomId = msg.readInt();

        if (!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            // fuck off
            client.getLogger().error(
                    ModToolUserInfoMessageEvent.class.getName() + " - tried to view room info for room: " + roomId);
            client.disconnect();
            return;
        }

        Room room = RoomManager.getInstance().get(roomId);

        if (room == null)
            return;

        client.send(ModToolRoomInfoMessageComposer.compose(room));
    }
}
