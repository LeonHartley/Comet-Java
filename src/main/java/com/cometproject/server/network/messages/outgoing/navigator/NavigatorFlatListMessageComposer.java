package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.*;

public class NavigatorFlatListMessageComposer {
    public static Composer compose(int category, int mode, String query, Collection<RoomData> activeRooms, boolean limit) {
        Composer msg = new Composer(Composers.NavigatorFlatListMessageComposer);
        msg.writeInt(mode);
        msg.writeString(query);
        msg.writeInt(limit ? (activeRooms.size() > 50 ? 50 : activeRooms.size()) : activeRooms.size());

        Collections.sort((List<RoomData>) activeRooms, new Comparator<RoomData>() {
            @Override
            public int compare(RoomData o1, RoomData o2) {
                boolean is1Active = CometManager.getRooms().isActive(o1.getId());
                boolean is2Active = CometManager.getRooms().isActive(o2.getId());

                return ((!is2Active ? 0 : CometManager.getRooms().get(o2.getId()).getEntities().playerCount()) -
                        (!is1Active ? 0 : CometManager.getRooms().get(o1.getId()).getEntities().playerCount()));
            }
        });

        List<RoomData> topRooms = new ArrayList<>();

        for (RoomData room : activeRooms) {
            if (topRooms.size() < 50 || !limit)
                topRooms.add(room);
        }

        for (RoomData room : topRooms) {
            RoomWriter.write(room, msg);
        }


        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(false);

        topRooms.clear();
        activeRooms.clear();

        return msg;
    }

    public static Composer compose(int category, int mode, String query, Collection<RoomData> activeRooms) {
        return compose(category, mode, query, activeRooms, true);
    }
}
