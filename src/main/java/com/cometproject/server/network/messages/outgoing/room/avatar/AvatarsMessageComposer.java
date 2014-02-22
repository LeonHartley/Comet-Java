package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.game.rooms.avatars.misc.AvatarWriter;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class AvatarsMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.RoomUsersMessageComposer);

        msg.writeInt(room.getEntities().count());

        for(GenericEntity entity : room.getEntities().getEntitiesCollection().values()) {
            AvatarWriter.write(entity, msg);
        }

        return msg;
    }
}
