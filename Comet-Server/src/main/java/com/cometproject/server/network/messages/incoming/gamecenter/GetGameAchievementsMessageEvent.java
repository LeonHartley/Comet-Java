package com.cometproject.server.network.messages.incoming.gamecenter;

import com.cometproject.server.composers.gamecenter.GameAchievementsMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class GetGameAchievementsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new GameAchievementsMessageComposer());
    }
}

