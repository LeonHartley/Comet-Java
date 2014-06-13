package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.logging.entries.RoomVisitLogEntry;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import org.joda.time.DateTime;

import java.util.List;

public class ModToolRoomVisitsMessageComposer {
    public static Composer compose(int userId, String username, List<RoomVisitLogEntry> roomVisits) {
        Composer msg = new Composer(Composers.ModToolRoomVisitsMessageComposer);

        msg.writeInt(userId);
        msg.writeString(username);

        msg.writeInt(roomVisits.size());

        for(RoomVisitLogEntry roomVisit : roomVisits) {
            RoomData roomData = CometManager.getRooms().getRoomData(roomVisit.getRoomId());
            DateTime dateTime = new DateTime(roomVisit.getEntryTime() * 1000L);

            msg.writeBoolean(false); // is room public?
            msg.writeInt(roomData.getId());
            msg.writeString(roomData.getName());

            msg.writeInt(dateTime.hourOfDay().get());
            msg.writeInt(dateTime.getMinuteOfHour());
        }

        return msg;
    }
}
