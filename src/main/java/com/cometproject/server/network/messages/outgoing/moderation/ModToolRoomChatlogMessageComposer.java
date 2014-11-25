package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class ModToolRoomChatlogMessageComposer {
    public static Composer compose(int roomId, String roomName, List<RoomChatLogEntry> chatLogs) {
        Composer msg = new Composer(Composers.ModerationToolRoomChatlogMessageComposer);

        msg.writeByte(1);
        msg.writeShort(3);
        msg.writeString("");

        msg.writeByte(0);
        msg.writeBoolean(false); // Is public

        msg.writeString("roomId");
        msg.writeByte(1);
        msg.writeInt(roomId);

        msg.writeString("roomName");
        msg.writeByte(2);
        msg.writeString(roomName);

        msg.writeShort(chatLogs.size());

        for(RoomChatLogEntry entry : chatLogs) {
            entry.compose(msg);
        }

        return msg;
    }
}
