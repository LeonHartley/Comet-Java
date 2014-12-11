package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.server.game.rooms.types.components.types.RoomBan;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class RoomBannedListMessageComposer {
    public static Composer compose(int roomId, List<RoomBan> bans) {
        Composer msg = new Composer(Composers.RoomBannedListMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(bans.size());

        for (RoomBan ban : bans) {
            msg.writeInt(ban.getPlayerId());
            msg.writeString(ban.getPlayerName());
        }

        return msg;
    }
}
