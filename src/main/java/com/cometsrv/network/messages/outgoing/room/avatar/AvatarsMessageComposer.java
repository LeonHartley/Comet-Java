package com.cometsrv.network.messages.outgoing.room.avatar;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.avatars.misc.AvatarWriter;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class AvatarsMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.RoomUsersMessageComposer);

        msg.writeInt(room.getAvatars().getAvatars().size());

        for(Avatar user : room.getAvatars().getAvatars().values()) {
            AvatarWriter.write(user, msg);
        }

        return msg;
    }
}
