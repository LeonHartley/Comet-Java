package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.moderation.chatlog.UserChatlogContainer;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ModToolUserChatlogMessageComposer {
    public static Composer compose(int userId, UserChatlogContainer logContainer) {
        Composer msg = new Composer(Composers.ModToolUserChatlogMessageComposer);

        String username = PlayerDao.getUsernameByPlayerId(userId);

        msg.writeInt(userId);
        msg.writeString(username);
        msg.writeInt(logContainer.size());

        for(UserChatlogContainer.LogSet logSet : logContainer.getLogs()) {
            RoomData roomData = CometManager.getRooms().getRoomData(logSet.getRoomId());

            msg.writeByte(1);
            msg.writeShort(2);
            msg.writeString("roomName");
            msg.writeByte(2); // type of following data (string = 2)
            msg.writeString(roomData.getName());
            msg.writeString("roomId");
            msg.writeByte(1); //type of following data i guess (int = 1)
            msg.writeInt(roomData.getId());

            msg.writeShort(logSet.getLogs().size());

            for(RoomChatLogEntry chatLogEntry : logSet.getLogs()) {
                //msg.writeInt((int) (Comet.getTime() / 1000) - chatLogEntry.getTimestamp()); // TODO: Fix the timestamps
                msg.writeInt((int) (Comet.getTime() - chatLogEntry.getTimestamp()) * 1000); // TODO: Fix the timestamps

                System.out.println(chatLogEntry.getTimestamp());

                msg.writeInt(chatLogEntry.getUserId());
                msg.writeString(username);
                msg.writeString(chatLogEntry.getString());
                msg.writeBoolean(false);
            }
        }

        return msg;
    }
}
