package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.server.game.rooms.types.components.types.RoomBan;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class RoomBannedListMessageComposer extends MessageComposer {
    private final int roomId;
    private final List<RoomBan> bans;

    public RoomBannedListMessageComposer(int roomId, List<RoomBan> bans) {
        this.roomId = roomId;
        this.bans = bans;
    }

    @Override
    public short getId() {
        return Composers.RoomBannedListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomId);
        msg.writeInt(bans.size());

        for (RoomBan ban : bans) {
            msg.writeInt(ban.getPlayerId());
            msg.writeString(ban.getPlayerName());
        }
    }
}
