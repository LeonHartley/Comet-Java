package com.cometproject.server.network.messages.incoming.moderation.tickets;

import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.moderation.tickets.ModToolTicketChatlogMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class ModToolTicketChatlogMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (!client.getPlayer().getPermissions().getRank().modTool()) {
            client.disconnect();
            return;
        }

        final int ticketId = msg.readInt();
        final HelpTicket helpTicket = ModerationManager.getInstance().getTicket(ticketId);

        if (helpTicket == null || helpTicket.getModeratorId() != client.getPlayer().getId()) {
            // Doesn't exist or already picked!
            return;
        }

        final RoomData roomData = RoomManager.getInstance().getRoomData(helpTicket.getRoomId());

        if (roomData == null) return;

        client.send(new ModToolTicketChatlogMessageComposer(helpTicket, helpTicket.getRoomId(), roomData.getName(), LogQueries.getChatlogsForRoom(roomData.getId(), helpTicket.getDateSubmitted() - (30 * 60), helpTicket.getDateSubmitted() + (10 * 60))));
    }
}
