package com.cometproject.server.network.messages.incoming.moderation.tickets;

import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.game.moderation.types.tickets.HelpTicketState;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.moderation.tickets.HelpTicketMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ModToolPickTicketMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        msg.readInt();
        int ticketId = msg.readInt();

        if(!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.disconnect();
            return;
        }

        final HelpTicket helpTicket = ModerationManager.getInstance().getTicket(ticketId);

        if(helpTicket == null || helpTicket.getModeratorId() != 0) {
            // Doesn't exist or already picked!
            return;
        }

        helpTicket.setModeratorId(client.getPlayer().getId());
        helpTicket.setState(HelpTicketState.IN_PROGRESS);
        helpTicket.save();

        NetworkManager.getInstance().getSessions().broadcastByPermission(new HelpTicketMessageComposer(helpTicket), "mod_tool");
    }
}
