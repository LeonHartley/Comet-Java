package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.game.moderation.chatlog.UserChatlogContainer;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.logging.entries.RoomVisitLogEntry;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolUserChatlogMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class ModToolUserChatlogMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        if (!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            return;
        }

        UserChatlogContainer chatlogContainer = new UserChatlogContainer();

        for (RoomVisitLogEntry visit : LogQueries.getLastRoomVisits(userId, 50)) {
            chatlogContainer.addAll(visit.getRoomId(), LogQueries.getChatlogsByCriteria(visit.getPlayerId(), visit.getRoomId(), visit.getEntryTime(), visit.getExitTime()));
        }

        client.send(ModToolUserChatlogMessageComposer.compose(userId, chatlogContainer));
    }
}
