package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
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
        msg.writeInt(activeRooms.size() > 50 ? 50 : activeRooms.size());

        Collection<Room> rooms = new FastList<>();

        int i = 0;
        for(Room room : activeRooms) {
            if(i >= 50) break;
            rooms.add(room);

            i++;
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
