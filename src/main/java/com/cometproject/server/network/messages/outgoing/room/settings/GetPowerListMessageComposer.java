package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.List;

public class GetPowerListMessageComposer {
    public static Composer compose(int roomId, List<Integer> usersWithRights) {
        Composer msg = new Composer(Composers.GetPowerListMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(usersWithRights.size());

        for (Integer id : usersWithRights) {
            msg.writeInt(id);

            String username = PlayerDao.getUsernameByPlayerId(id);
            msg.writeString(username != null ? username : "Placeholder");
        }


        return msg;
    }
}
