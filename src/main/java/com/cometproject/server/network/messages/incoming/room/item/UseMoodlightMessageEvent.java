package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.objects.items.types.wall.MoodlightWallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.items.moodlight.MoodlightMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


<<<<<<< HEAD
public class UseMoodlightMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        Room room = client.getPlayer().getEntity().getRoom();
=======
public class UseMoodlightMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if(client.getPlayer() == null || client.getPlayer().getEntity() == null) return;
>>>>>>> origin/master

        if(client.getPlayer().getEntity().getRoom() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();

        if (!room.getRights().hasRights(client.getPlayer().getEntity().getPlayerId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        MoodlightWallItem moodlight = room.getItems().getMoodlight();

        if (moodlight == null) {
            return;
        }

        if (moodlight.getMoodlightData() == null) return;
        client.send(new MoodlightMessageComposer(moodlight));
    }
}
