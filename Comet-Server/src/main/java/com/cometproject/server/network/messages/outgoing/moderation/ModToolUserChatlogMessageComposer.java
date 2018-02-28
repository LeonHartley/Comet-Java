package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.moderation.chatlog.UserChatlogContainer;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class ModToolUserChatlogMessageComposer extends MessageComposer {
    private final static String ROOM_ID = "roomId";
    private final static String ROOM_NAME = "roomName";
    private final static String ROOM_NAME_UNKNOWN = "Unknown Room";

    private final int playerId;
    private final UserChatlogContainer userChatlogContainer;

    public ModToolUserChatlogMessageComposer(final int playerId, final UserChatlogContainer userChatlogContainer) {
        this.playerId = playerId;
        this.userChatlogContainer = userChatlogContainer;
    }

    @Override
    public short getId() {
        return Composers.ModeratorUserChatlogMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        String username = PlayerDao.getUsernameByPlayerId(this.playerId);

        msg.writeInt(this.playerId);
        msg.writeString(username);
        msg.writeInt(this.userChatlogContainer.size());

        for (UserChatlogContainer.LogSet logSet : this.userChatlogContainer.getLogs()) {
            IRoomData roomData = GameContext.getCurrent().getRoomService().getRoomData(logSet.getRoomId());
            msg.writeByte(1);
            msg.writeShort(2);
            msg.writeString(ROOM_NAME);
            msg.writeByte(2); // type of following data (string = 2)
            msg.writeString(roomData == null ? ROOM_NAME_UNKNOWN : roomData.getName());
            msg.writeString(ROOM_ID);
            msg.writeByte(1); //type of following data i guess (int = 1)
            msg.writeInt(roomData == null ? 0 : roomData.getId());

            msg.writeShort(logSet.getLogs().size());

            for (RoomChatLogEntry chatLogEntry : logSet.getLogs()) {
                chatLogEntry.compose(msg);
            }
        }
    }
}
