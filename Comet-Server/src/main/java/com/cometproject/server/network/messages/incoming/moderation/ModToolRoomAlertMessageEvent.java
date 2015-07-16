package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class ModToolRoomAlertMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int action = msg.readInt();

        String alert = msg.readString();
        String reason = msg.readString();

        if (!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.disconnect();
            return;
        }

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null)
            return;

        Room room = client.getPlayer().getEntity().getRoom();

        room.getEntities().broadcastMessage(new AlertMessageComposer(alert));
        // TODO: Log these
    }
}
