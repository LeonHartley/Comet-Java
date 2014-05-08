package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.avatar.ShoutMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ShoutMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String message = msg.readString();
        int colour = msg.readInt();

        if (!TalkMessageEvent.isValidColour(colour, client)) {
            colour = 0;
        }

        String filteredMessage = TalkMessageEvent.filterMessage(message);

        if (!client.getPlayer().getPermissions().hasPermission("bypass_filter")) {
            filteredMessage = CometManager.getRooms().getFilter().filter(message);
        }

        if (client.getPlayer().getEntity().onChat(filteredMessage)) {
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(ShoutMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), filteredMessage, CometManager.getRooms().getEmotions().getEmotion(filteredMessage), colour));
        }
    }
}
