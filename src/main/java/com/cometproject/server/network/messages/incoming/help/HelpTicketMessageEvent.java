package com.cometproject.server.network.messages.incoming.help;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.help.TicketSentMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class HelpTicketMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        boolean hasActiveTicket = CometManager.getModeration().getTicketByUserId(client.getPlayer().getId()) != null;

//        if (hasActiveTicket) {
//            client.send(AdvancedAlertMessageComposer.compose(Locale.get("help.ticket.pending.title"), Locale.get("help.ticket.pending.message")));
//            return;
//        }

        String message = msg.readString();
//        int unk = msg.readInt();
        int category = msg.readInt();
        int reportedId = msg.readInt();
//        int timestamp = (int) Comet.getTime();
//        int roomId = client.getPlayer().getEntity() != null ? client.getPlayer().getEntity().getRoom().getId() : 0;

        int junk = msg.readInt();
        int chatCout = msg.readInt();

        for(int i = 0; i < chatCout; i++) {
            int sayerId = msg.readInt();

            System.out.println(o);

            System.out.println(msg.readString());
        }

        client.send(TicketSentMessageComposer.compose());
    }
}