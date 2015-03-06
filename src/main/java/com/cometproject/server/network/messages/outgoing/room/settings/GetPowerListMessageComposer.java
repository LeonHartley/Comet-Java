package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.List;


public class GetPowerListMessageComposer extends MessageComposer {
    private final int roomId;
    private final List<Integer> playersWithRights;

    public GetPowerListMessageComposer(int roomId, List<Integer> playersWithRights) {
        this.roomId = roomId;
        this.playersWithRights = playersWithRights;
    }

    @Override
    public short getId() {
        return Composers.LoadRoomRightsListMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(roomId);
        msg.writeInt(playersWithRights.size());

        for (Integer id : playersWithRights) {
            msg.writeInt(id);

            String username = PlayerDao.getUsernameByPlayerId(id);
            msg.writeString(username != null ? username : "Placeholder");
        }
    }
}
