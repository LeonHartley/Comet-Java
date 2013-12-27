package com.cometsrv.network.messages.outgoing.room.settings;

import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomWriter;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class GetRoomDataMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.GetRoomDataMessageComposer);

        msg.writeInt(room.getId());
        msg.writeString(room.getData().getName());
        msg.writeString(room.getData().getDescription());
        msg.writeInt(RoomWriter.roomAccessToNumber(room.getData().getAccess()));
        msg.writeInt(room.getData().getCategory().getId());
        msg.writeInt(room.getData().getMaxUsers());
        msg.writeInt(room.getData().getMaxUsers());
        msg.writeInt(room.getData().getTags().length);

        for(String tag : room.getData().getTags()) {
            msg.writeString(tag);
        }

        msg.writeInt(0); // TODO: rights
        msg.writeInt(1); // allow pets
        msg.writeInt(1); // allow pets eat
        msg.writeInt(1); // allow walk through
        msg.writeInt(room.getData().getHideWalls() ? 1 : 0);
        msg.writeInt(room.getData().getWallThickness());
        msg.writeInt(room.getData().getFloorThickness());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);

        return msg;
    }
}
