package com.cometproject.server.network.messages.incoming.help;

import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.game.moderation.types.tickets.HelpTicketState;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.help.InitHelpToolMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class DeletePendingTicketMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        final HelpTicket helpTicket = ModerationManager.getInstance().getActiveTicketByPlayerId(client.getPlayer().getId());

        if(helpTicket != null) {
            helpTicket.setState(HelpTicketState.CLOSED);
            helpTicket.save();

            ModerationManager.getInstance().getTickets().remove(helpTicket.getId());
            client.send(new InitHelpToolMessageComposer(null));
        }
    }
}
