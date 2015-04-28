package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;


public class UserBadgesMessageComposer extends MessageComposer {
    private final int playerId;
    private final Map<String, Integer> badges;

    public UserBadgesMessageComposer(final int playerId, final Map<String, Integer> badges) {
        this.playerId = playerId;
        this.badges = badges;
    }

    @Override
    public short getId() {
        return Composers.UserBadgesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(playerId);
        msg.writeInt(badges.size());

        for (Map.Entry<String, Integer> badge : badges.entrySet()) {
            if (badge.getValue() > 0) {
                msg.writeInt(badge.getValue());
                msg.writeString(badge.getKey());
            }
        }
    }
}
