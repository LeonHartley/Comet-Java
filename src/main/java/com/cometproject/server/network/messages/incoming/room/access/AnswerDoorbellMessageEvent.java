package com.cometproject.server.network.messages.incoming.room.access;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.access.DoorbellAcceptedComposer;
import com.cometproject.server.network.messages.outgoing.room.alerts.DoorbellNoAnswerComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class AnswerDoorbellMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if (client.getPlayer().getEntity() == null) {
            return;
        }

        if (client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        String username = msg.readString();
        boolean answered = msg.readBoolean();

        if (username.equals("")) {
            return;
        }

        Session requestingClient = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        // player could of d/c
        if (requestingClient == null) {
            return;
        }

        if(requestingClient.getPlayer() == null || requestingClient.getPlayer().getEntity() == null) {
            return;
        }

        // already answered ?
        if (requestingClient.getPlayer().getEntity().isDoorbellAnswered()) {
            return;
        }

        // still ringing for this room ?
        if (requestingClient.getPlayer().getEntity().getRoom().getId() != client.getPlayer().getEntity().getRoom().getId()) {
            return;
        }

        if (answered) {
            requestingClient.getPlayer().getEntity().setDoorbellAnswered(true);
            requestingClient.send(DoorbellAcceptedComposer.compose());
        } else {
            requestingClient.send(DoorbellNoAnswerComposer.compose());
            requestingClient.send(HotelViewMessageComposer.compose());
        }
    }
}
