package com.cometproject.network.messages.outgoing.room.avatar;

import com.cometproject.game.rooms.avatars.misc.AvatarWriter;
import com.cometproject.game.rooms.entities.GenericEntity;
import com.cometproject.game.rooms.types.Room;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

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
