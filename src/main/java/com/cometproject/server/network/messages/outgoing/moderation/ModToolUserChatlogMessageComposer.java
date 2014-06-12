package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.moderation.chatlog.UserChatlogContainer;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

public class ModToolUserChatlogMessageComposer {
    public static Composer compose(int userId, UserChatlogContainer logContainer) {
        Composer msg = new Composer(Composers.ModToolUserChatlogMessageComposer);

        String username = PlayerDao.getUsernameByPlayerId(userId);

        msg.writeInt(userId);
        msg.writeString(username);
        msg.writeInt(logContainer.size());

        for(Map.Entry<Integer, List<RoomChatLogEntry>> log : logContainer.getLogs().entrySet()) {
            RoomData roomData = CometManager.getRooms().getRoomData(log.getKey());

            msg.writeByte(1);
            msg.writeShort(2);
            msg.writeString("roomName");
            msg.writeByte(2); // type of following data (string = 2)
            msg.writeString(roomData.getName());
            msg.writeString("roomId");
            msg.writeByte(1); //type of following data i guess (int = 1)
            msg.writeInt(roomData.getId());

            msg.writeShort(log.getValue().size());

            for(RoomChatLogEntry chatLogEntry : log.getValue()) {
                msg.writeInt((int) (System.currentTimeMillis() - (chatLogEntry.getTimestamp() * 1000L)));
                msg.writeInt(chatLogEntry.getUserId());
                msg.writeString(username);
                msg.writeString(chatLogEntry.getString());
                msg.writeBoolean(false);
            }
        }

        return msg;
    }
}
