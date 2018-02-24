package com.cometproject.server.network.messages.incoming.navigator.updated;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class NavigatorSaveViewModeMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final String category = msg.readString();
        final int viewMode = msg.readInt();

        client.getPlayer().getNavigator().getViewModes().put(category, viewMode);
        PlayerDao.saveViewMode(category, viewMode, client.getPlayer().getId());
    }
}
