package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class WisperMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String text = msg.readString();

        String user = text.split(" ")[0];
        String message = text.replace(user + " ", "");
        Session userTo = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(user);

        if (userTo == null || userTo == client)
            return;

        String filteredMessage = TalkMessageEvent.filterMessage(message);

        if (!client.getPlayer().getEntity().onChat(filteredMessage))
            return;

        client.send(WisperMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), filteredMessage));
        userTo.send(WisperMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), filteredMessage));
    }
}
