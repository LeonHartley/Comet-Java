package com.cometproject.server.network.messages.incoming.landing;

import com.cometproject.server.game.landing.LandingManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.landing.PromoArticlesMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class RefreshPromoArticlesMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.send(new PromoArticlesMessageComposer(LandingManager.getInstance().getArticles()));
    }
}
