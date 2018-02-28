package com.cometproject.server.network.messages.incoming.help;

import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.help.InitHelpToolMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class InitHelpToolMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        client.send(new InitHelpToolMessageComposer(ModerationManager.getInstance().getActiveTicketByPlayerId(client.getPlayer().getId())));
    }
}
