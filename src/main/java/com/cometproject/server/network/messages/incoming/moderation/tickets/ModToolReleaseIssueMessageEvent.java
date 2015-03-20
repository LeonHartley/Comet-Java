package com.cometproject.server.network.messages.incoming.moderation.tickets;

import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.game.moderation.types.tickets.HelpTicketState;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ModToolReleaseIssueMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int ticketCount = msg.readInt();
        if(!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.disconnect();
            return;
        }

        for(int i = 0; i < ticketCount; i++) {
            int ticketId = msg.readInt();

            final HelpTicket helpTicket = ModerationManager.getInstance().getTicket(ticketId);

            if (helpTicket == null || helpTicket.getModeratorId() != client.getPlayer().getId()) return;

            helpTicket.setState(HelpTicketState.OPEN);
            helpTicket.setModeratorId(0);

            helpTicket.save();

            ModerationManager.getInstance().broadcastTicket(helpTicket);
        }
    }
}
