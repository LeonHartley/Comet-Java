package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.items.types.wall.MoodlightWallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ToggleMoodlightMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null) {
            return;
        }

        MoodlightWallItem moodlight = room.getItems().getMoodlight();
        if (moodlight == null) {
            return;
        }

        if (!moodlight.getMoodlightData().isEnabled()) {
            moodlight.getMoodlightData().setEnabled(true);
        } else {
            moodlight.getMoodlightData().setEnabled(false);
        }

        moodlight.setExtraData(moodlight.generateExtraData());
        moodlight.sendUpdate();
    }
}
