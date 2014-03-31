package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ModToolRoomInfoMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.ModToolRoomInfoMessageComposer);

        msg.writeInt(room.getId());
        msg.writeInt(room.isActive ? room.getEntities().playerCount() : 0);

        msg.writeBoolean(Comet.getServer().getNetwork().getSessions().isPlayerLogged(room.getData().getOwnerId()));
        msg.writeInt(room.getData().getOwnerId());
        msg.writeString(room.getData().getOwner());
        msg.writeBoolean(room.isActive);

        msg.writeString(room.getData().getName());
        msg.writeString(room.getData().getDescription());
        msg.writeInt(room.getData().getTags().length);

        for(int i = 0; i < room.getData().getTags().length; i++) {
            msg.writeString(room.getData().getTags()[i]);
        }

        msg.writeBoolean(false); // has event

        return msg;
    }
}
