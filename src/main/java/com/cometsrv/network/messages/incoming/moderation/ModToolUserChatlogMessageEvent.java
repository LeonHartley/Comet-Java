package com.cometsrv.network.messages.incoming.moderation;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class ModToolUserChatlogMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        if(!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            return;
        }


    }
}
