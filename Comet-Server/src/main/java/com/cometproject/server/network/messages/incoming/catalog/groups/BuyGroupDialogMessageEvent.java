package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.composers.catalog.groups.GroupElementsMessageComposer;
import com.cometproject.server.composers.catalog.groups.GroupPartsMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class BuyGroupDialogMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        client.send(new GroupPartsMessageComposer(client.getPlayer().getRooms()));
        client.send(new GroupElementsMessageComposer(GroupManager.getInstance().getGroupItems()));
    }
}
