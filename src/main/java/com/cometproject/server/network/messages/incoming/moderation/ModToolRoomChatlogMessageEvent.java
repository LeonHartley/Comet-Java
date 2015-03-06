package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolRoomChatlogMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class ModToolRoomChatlogMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int context = msg.readInt();
        int roomId = msg.readInt();

        if (!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.disconnect();
            return;
        }

        if(!LogManager.ENABLED) {
            client.send(new AdvancedAlertMessageComposer("Notice", "Logging is not currently enabled, please contact your system administrator to enable it."));
            return;
        }

        RoomData roomData = RoomManager.getInstance().getRoomData(roomId);

        if (roomData != null) {
            client.send(new ModToolRoomChatlogMessageComposer(roomData.getId(), roomData.getName(), LogQueries.getChatlogsForRoom(roomData.getId())));
        } else {
            client.send(new AdvancedAlertMessageComposer("Notice", "There seems to be an issue with fetching the logs for this room (ID: " + roomId + ", Context: " + context + ")"));
        }
    }
}
