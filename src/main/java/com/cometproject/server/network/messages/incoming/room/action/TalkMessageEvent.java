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

        String filteredMessage = filterMessage(message);

        if (client.getPlayer().getEntity().onChat(filteredMessage)) {
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(TalkMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), filteredMessage, GameEngine.getRooms().getEmotions().getEmotion(filteredMessage), colour));
        }
    }

    private static int[] allowedColours = new int[] {
      0, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 29
    };

    public static boolean isValidColour(int colour, Session client) {
        if(!Ints.contains(allowedColours, colour)) {
            return false;
        }

        if (colour == 23 && !client.getPlayer().getPermissions().hasPermission("mod_tool"))
            return false;

        return true;
    }

    public static String filterMessage(String message) {
        return message.replace((char)13 + "", "");
    }
}