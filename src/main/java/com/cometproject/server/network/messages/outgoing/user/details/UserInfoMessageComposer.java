package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Date;

public class UserInfoMessageComposer {
    public static Composer compose(Player player) {
        Composer msg = new Composer(Composers.UserInfoMessageComposer);

        msg.writeInt(player.getId());
        msg.writeString(player.getData().getUsername());
        msg.writeString(player.getData().getFigure());
        msg.writeString(player.getData().getGender().toUpperCase());
        msg.writeString(player.getData().getMotto());
        msg.writeString(player.getData().getUsername().toLowerCase());
        msg.writeBoolean(true);

        msg.writeInt(8); // ??? (8)

        msg.writeInt(player.getStats().getDailyRespects()); // daily respects!
        msg.writeInt(3); // (3) pet respects I guess

        msg.writeBoolean(true);
        msg.writeString(new Date(player.getData().getLastVisit() * 1000L).toString());

        msg.writeBoolean(false);
        msg.writeBoolean(false);
        return msg;
    }
}
