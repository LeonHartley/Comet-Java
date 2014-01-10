package com.cometsrv.network.messages.outgoing.navigator;

import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomWriter;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;
import javolution.util.FastList;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NavigatorFlatListMessageComposer {
    public static Composer compose(int category, int mode, String query, Collection<Room> activeRooms) {
        Composer msg = new Composer(Composers.NavigatorFlatListMessageComposer);
        msg.writeInt(mode);
        msg.writeString(query);
        msg.writeInt(activeRooms.size());

        Collection<Room> rooms = new FastList<>();

        for(Room room : activeRooms) {
            rooms.add(room);
        }

        Collections.sort((List<Room>) rooms, new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                return ((o2.getEntities() == null ? 0 : o2.getEntities().playerCount()) -
                                (o1.getEntities() == null ? 0 : o1.getEntities().playerCount()));
            }
        });

        for(Room room : rooms) {
            RoomWriter.write(room, msg);
        }

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(false);

        return msg;
    }
}
