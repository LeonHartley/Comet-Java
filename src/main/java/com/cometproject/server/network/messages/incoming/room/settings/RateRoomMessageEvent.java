package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class RateRoomMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if (client.getPlayer().getEntity() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        if (!client.getPlayer().getEntity().canRateRoom()) {
            return;
        }

        room.getRatings().add(client.getPlayer().getId());

        int direction = msg.readInt();
        int score = room.getData().getScore();

        if (direction == 1)
            score++;
        else
            score--;

        room.getData().setScore(score);
        room.getEntities().refreshScore();

        room.getData().save();
    }
}
