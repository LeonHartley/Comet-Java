package com.cometsrv.network.messages.incoming.user.inventory;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class BotInventoryMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(BotInventoryMessageComposer.compose(client.getPlayer().getBots().getBots()));
    }
}
