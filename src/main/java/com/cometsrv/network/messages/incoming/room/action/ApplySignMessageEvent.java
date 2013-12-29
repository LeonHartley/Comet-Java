package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class ApplySignMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.getPlayer().getAvatar().unidle();

        client.getPlayer().getAvatar().signTime = 3;
        client.getPlayer().getAvatar().getStatuses().put("sign", String.valueOf(msg.readInt()));
        client.getPlayer().getAvatar().needsUpdate = true;
    }
}
