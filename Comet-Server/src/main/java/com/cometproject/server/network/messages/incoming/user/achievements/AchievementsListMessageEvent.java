package com.cometproject.server.network.messages.incoming.user.achievements;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.achievements.AchievementsListMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class AchievementsListMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new AchievementsListMessageComposer(client.getPlayer().getAchievements()));
    }
}
