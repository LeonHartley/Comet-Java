package com.cometproject.server.network.messages.incoming.landing;

import com.cometproject.server.game.landing.LandingManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.landing.PromoArticlesMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class RefreshPromoArticlesMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new PromoArticlesMessageComposer(LandingManager.getInstance().getArticles()));
    }
}
