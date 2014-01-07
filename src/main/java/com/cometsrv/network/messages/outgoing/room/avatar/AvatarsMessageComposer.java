package com.cometsrv.network.messages.outgoing.room.avatar;

import com.cometsrv.game.rooms.avatars.misc.AvatarWriter;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

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
