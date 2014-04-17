package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class GetPowerListMessageComposer {
    public static Composer compose(int roomId, List<Integer> usersWithRights) {
        Composer msg = new Composer(Composers.GetPowerListMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(usersWithRights.size());

        for (Integer id : usersWithRights) {
            msg.writeInt(id);

            String username = Comet.getServer().getStorage().getString("SELECT `username` FROM players WHERE id = " + id);
            msg.writeString(username != null ? username : "Placeholder");
        }


        return msg;
    }
}
