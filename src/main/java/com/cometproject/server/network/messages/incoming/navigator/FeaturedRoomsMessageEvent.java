package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.FeaturedRoomsMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class FeaturedRoomsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        /*if (CometCache.getManager().isCacheEnabled()) {
           byte[] cachedBytes = CometCache.getManager().getComposerCacheHandler().get("featured_rooms");

            if (cachedBytes != null) {
                client.send(new Composer(cachedBytes));
                return;
            }
        }*/

        Composer newComposer = FeaturedRoomsMessageComposer.compose(GameEngine.getNavigator().getFeaturedRooms());

        /*if (CometCache.getManager().isCacheEnabled()) {
            CometCache.getManager().getComposerCacheHandler().put("featured_rooms", newComposer.get().array(), 5, TimeUnit.MINUTES);
        }*/

        client.send(newComposer);
    }
}
