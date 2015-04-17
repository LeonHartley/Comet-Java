package com.cometproject.server.network.messages.outgoing.user.club;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.SubscriptionComponent;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class ClubStatusMessageComposer extends MessageComposer {
    private final SubscriptionComponent subscriptionComponent;

    public ClubStatusMessageComposer(final SubscriptionComponent subscriptionComponent) {
        this.subscriptionComponent = subscriptionComponent;
    }

    @Override
    public short getId() {
        return Composers.SubscriptionStatusMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        int timeLeft = 0;
        int days = 0;
        int months = 0;

        if (subscriptionComponent.isValid()) {
            timeLeft = subscriptionComponent.getExpire() - (int) Comet.getTime();
            days = (int) Math.ceil(timeLeft / 86400);
            months = days / 31;

            if (months >= 1) {
                months--;
            }
        } else {
            if (subscriptionComponent.exists()) {
                subscriptionComponent.delete();
            }
        }

        msg.writeString("club_habbo");

        msg.writeInt(subscriptionComponent.isValid() ? days - (months * 31) : 0);
        msg.writeInt(2);
        msg.writeInt(subscriptionComponent.isValid() ? months : 0);
        msg.writeInt(1);
        msg.writeBoolean(subscriptionComponent.isValid());
        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(subscriptionComponent.isValid() ? days - (months * 31) : 0);
        msg.writeInt(subscriptionComponent.isValid() ? days - (months * 31) : 0);

    }
}
