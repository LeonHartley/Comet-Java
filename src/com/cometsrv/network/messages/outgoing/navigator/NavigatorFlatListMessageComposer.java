package com.cometsrv.network.messages.outgoing.navigator;

import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomWriter;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

import java.util.Collection;

public class NavigatorFlatListMessageComposer {
    public static Composer compose(int category, int mode, String query, Collection<Room> rooms) {
        Composer msg = new Composer(Composers.NavigatorFlatListMessageComposer);
        msg.writeInt(mode);
        msg.writeString(query);
        msg.writeInt(rooms.size());

        for(Room room : rooms) {
            RoomWriter.write(room, msg);
        }

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(false);

        return msg;
    }
}
