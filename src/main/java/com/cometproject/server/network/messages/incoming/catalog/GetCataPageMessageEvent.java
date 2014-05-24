package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.CataPageMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GetCataPageMessageEvent implements IEvent {
    private Composer cachedSpaces;

    public void handle(Session client, Event msg) {
        int pageId = msg.readInt();

        if (CometManager.getCatalog().pageExists(pageId) && CometManager.getCatalog().getPage(pageId).isEnabled()) {
            // TODO: better caching for other pages + all the other "static" composers
            if (CometManager.getCatalog().getPage(pageId).getTemplate().equals("spaces_new")) {
                if (cachedSpaces != null) {
                    client.send(cachedSpaces.duplicate());
                } else {
                    cachedSpaces = CataPageMessageComposer.compose(CometManager.getCatalog().getPage(pageId));
                    client.send(cachedSpaces);
                }

                return;
            }

            client.send(CataPageMessageComposer.compose(CometManager.getCatalog().getPage(pageId)));
        }
    }
}
