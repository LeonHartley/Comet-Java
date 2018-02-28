package com.cometproject.server.network.messages.incoming.navigator.updated;

import com.cometproject.server.game.navigator.types.search.NavigatorSearchService;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class NewNavigatorSearchMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String category = msg.readString();
        String data = msg.readString();


        NavigatorSearchService.getInstance().submitRequest(client.getPlayer(), category, data);
    }
}
