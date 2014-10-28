package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ModToolRoomInfoMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.ModerationRoomToolMessageComposer);

        msg.writeInt(room.getId());
        msg.writeInt(room.getEntities().playerCount());

        msg.writeBoolean(CometManager.getPlayers().isOnline(room.getData().getOwnerId()));
        msg.writeInt(room.getData().getOwnerId());
        msg.writeString(room.getData().getOwner());
        msg.writeBoolean(true); // TODO: Allow for rooms that aren't active to show here ;-)

        msg.writeString(room.getData().getName());
        msg.writeString(room.getData().getDescription());
        msg.writeInt(room.getData().getTags().length);

        for (int i = 0; i < room.getData().getTags().length; i++) {
            msg.writeString(room.getData().getTags()[i]);
        }

        msg.writeBoolean(false); // has event

        return msg;
    }
}
