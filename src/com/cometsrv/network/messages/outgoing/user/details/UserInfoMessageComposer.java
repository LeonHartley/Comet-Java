package com.cometsrv.network.messages.outgoing.user.details;

import com.cometsrv.game.players.types.Player;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

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
        msg.writeInt(1); // (3) pet respects I guess

        msg.writeBoolean(true);
        msg.writeString(player.getData().getLastVisit());

        msg.writeBoolean(false);
        msg.writeBoolean(false);
        return msg;
    }
}
