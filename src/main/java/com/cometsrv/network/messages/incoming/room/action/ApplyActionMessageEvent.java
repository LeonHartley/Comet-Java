package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class ApplyActionMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if(client.getPlayer().getAvatar().getRoom() != null) {
            int actionId = msg.readInt();

            if(actionId == 5) {
                client.getPlayer().getAvatar().idle();
            } else {
                client.getPlayer().getAvatar().unidle();
            }

            client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(ActionMessageComposer.compose(client.getPlayer().getId(), actionId));
        }
    }
}
