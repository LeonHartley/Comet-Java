package com.cometproject.server.network.messages.outgoing.catalog.groups;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;
import java.util.Map;

public class GroupPartsMessageComposer {
    public static Composer compose(List<Integer> rooms) {
        Composer msg = new Composer(Composers.GroupPartsMessageComposer);

        int roomCount = 0;

        for (Integer room : rooms) {
            //if (room.getGroup() == null) {
            roomCount++;
           //}
        }

        msg.writeInt(CometSettings.groupCost);
        msg.writeInt(roomCount);

        for (Integer room : rooms) {
            RoomData roomData = CometManager.getRooms().getRoomData(room);
            //if (room.getGroup() == null) {
            msg.writeInt(roomData.getId());
            msg.writeString(roomData.getName());
            msg.writeBoolean(false);
            //}
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
