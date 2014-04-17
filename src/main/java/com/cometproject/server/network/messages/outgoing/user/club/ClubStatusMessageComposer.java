package com.cometproject.server.network.messages.outgoing.user.club;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.SubscriptionComponent;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ClubStatusMessageComposer {
    public static Composer compose(SubscriptionComponent subscription) {
        int timeLeft = 0;
        int days = 0;
        int months = 0;

        if (subscription.isValid()) {
            timeLeft = subscription.getExpire() - (int) Comet.getTime();
            days = (int) Math.ceil(timeLeft / 86400);
            months = days / 31;

            if (months >= 1) {
                months--;
            }
        } else {
            if (subscription.exists()) {
                subscription.delete();
            }
        }

        Composer msg = new Composer(Composers.SerializeClubMessageComposer);
        msg.writeString("club_habbo");

        msg.writeInt(subscription.isValid() ? days - (months * 31) : 0);
        msg.writeInt(2);
        msg.writeInt(subscription.isValid() ? months : 0);
        msg.writeInt(1);
        msg.writeBoolean(subscription.isValid());
        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(subscription.isValid() ? days - (months * 31) : 0);
        msg.writeInt(subscription.isValid() ? days - (months * 31) : 0);

        return msg;
    }
}
