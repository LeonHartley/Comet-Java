package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.game.GameEngine;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class TalkMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String message = msg.readString();
        int colour = msg.readInt();

        if(!TalkMessageEvent.isValidColour(colour, client))
            colour = 0;

        if(client.getPlayer().getEntity().onChat(message)) {
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(TalkMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), message, GameEngine.getRooms().getEmotions().getEmotion(message), colour));
        }
    }

    public static boolean isValidColour(int colour, Session client) {
        if(colour >= 24)
            return false;

        if(colour == 1 || colour == 2)
            return false;

        if(colour == 23 && !client.getPlayer().getPermissions().hasPermission("mod_tool"))
            return false;

            return true;
    }
}