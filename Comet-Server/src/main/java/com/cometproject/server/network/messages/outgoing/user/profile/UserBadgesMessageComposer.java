package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

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
        return Composers.HabboUserBadgesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(playerId);
        msg.writeInt(badges.size());

        badges.forEach((badge, slot) -> {
            if(slot > 0) {
                msg.writeInt(slot);
                msg.writeString(badge);
            }
        });
    }
}
