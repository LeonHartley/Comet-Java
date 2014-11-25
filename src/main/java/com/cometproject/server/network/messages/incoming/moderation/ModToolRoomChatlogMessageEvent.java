package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.moderation.chatlog.UserChatlogContainer;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.logging.entries.RoomVisitLogEntry;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolRoomChatlogMessageComposer;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolUserChatlogMessageComposer;
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

        RoomData roomData = CometManager.getRooms().getRoomData(roomId);

        if(roomData != null) {
            client.send(ModToolRoomChatlogMessageComposer.compose(roomData.getId(), roomData.getName(), LogQueries.getChatlogsForRoom(roomData.getId())));
        }
    }
}
