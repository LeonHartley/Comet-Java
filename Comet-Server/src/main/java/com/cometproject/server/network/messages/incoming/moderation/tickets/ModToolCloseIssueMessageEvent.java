package com.cometproject.server.network.messages.incoming.moderation.tickets;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.game.moderation.types.tickets.HelpTicketState;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.moderation.tickets.HelpTicketResponseMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class ModToolCloseIssueMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int result = msg.readInt();
        final int unk = msg.readInt();
        final int ticketId = msg.readInt();

        if (!client.getPlayer().getPermissions().getRank().modTool()) {
            client.disconnect();
            return;
        }

        final HelpTicket helpTicket = ModerationManager.getInstance().getTicket(ticketId);

        if (helpTicket.getModeratorId() != client.getPlayer().getId()) return;

        final PlayerStatistics submitterStatistics;
        final Session submitterSession;

        if (PlayerManager.getInstance().isOnline(helpTicket.getSubmitterId())) {
            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(helpTicket.getSubmitterId());

            if (session != null && session.getPlayer() != null && session.getPlayer().getStats() != null) {
                submitterSession = session;
                submitterStatistics = session.getPlayer().getStats();
            } else {
                submitterSession = null;
                submitterStatistics = PlayerDao.getStatisticsById(helpTicket.getSubmitterId());
            }
        } else {
            submitterSession = null;
            submitterStatistics = PlayerDao.getStatisticsById(helpTicket.getSubmitterId());
        }

        switch (result) {
            default:
            case 0: // Resolved
                helpTicket.setState(HelpTicketState.RESOLVED);
                break;

            case 1: // Invalid
                helpTicket.setState(HelpTicketState.INVALID);
                break;

            case 2: // Abusive
                if (submitterStatistics != null) {
                    submitterStatistics.incrementAbusiveHelpTickets(1);
                    submitterStatistics.save();
                }

                helpTicket.setState(HelpTicketState.ABUSIVE);
                break;
        }

        if (submitterSession != null) {
            submitterSession.send(new HelpTicketResponseMessageComposer(result));
        }

        helpTicket.setDateClosed((int) Comet.getTime());
        helpTicket.save();

        ModerationManager.getInstance().broadcastTicket(helpTicket);
        ModerationManager.getInstance().getTickets().remove(helpTicket.getId());
    }
}
