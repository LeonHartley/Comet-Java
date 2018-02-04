package com.cometproject.server.network.messages.incoming.gamecenter;

import com.cometproject.server.composers.gamecenter.GameListMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class GetGameListMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new GameListMessageComposer());
    }
}
