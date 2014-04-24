package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.google.common.primitives.Ints;


public class TalkMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String message = msg.readString();
        int colour = msg.readInt();

        if (!TalkMessageEvent.isValidColour(colour, client))
            colour = 0;

        if (client.getPlayer().getEntity().onChat(message)) {
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(TalkMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), message, GameEngine.getRooms().getEmotions().getEmotion(message), colour));
        }
    }

    private static int[] allowedColours = new int[] {
            0, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23
    };

    public static boolean isValidColour(int colour, Session client) {
        return colour < 24 && Ints.contains(allowedColours, colour) && !(colour == 23 && !client.getPlayer().getPermissions().hasPermission("mod_tool"));
    }
}