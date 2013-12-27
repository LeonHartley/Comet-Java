package com.cometsrv.network.messages.outgoing.catalog.groups;

import com.cometsrv.config.CometSettings;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

import java.util.Map;

public class GroupPartsMessageComposer {
    public static Composer compose(Map<Integer, Room> rooms) {
        Composer msg = new Composer(Composers.GroupPartsMessageComposer);

        int roomCount = 0;

        for(Room room : rooms.values()) {
            if(room.getGroup() == null) {
                roomCount++;
            }
        }

        msg.writeInt(CometSettings.groupCost);
        msg.writeInt(roomCount);

        for(Room room : rooms.values()) {
            if(room.getGroup() == null) {
                msg.writeInt(room.getId());
                msg.writeString(room.getData().getName());
                msg.writeBoolean(false);
            }
        }

        msg.writeInt(5);
        msg.writeInt(10);
        msg.writeInt(3);
        msg.writeInt(4);
        msg.writeInt(0x19);
        msg.writeInt(0x11);
        msg.writeInt(5);
        msg.writeInt(0x19);
        msg.writeInt(0x11);
        msg.writeInt(3);
        msg.writeInt(0x1d);
        msg.writeInt(11);
        msg.writeInt(4);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);

        return msg;
    }
}
