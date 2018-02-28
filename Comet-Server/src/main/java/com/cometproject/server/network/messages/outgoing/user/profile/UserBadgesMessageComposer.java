package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class UserBadgesMessageComposer extends MessageComposer {
    private final int playerId;
    private final String[] badges;

    public UserBadgesMessageComposer(final int playerId, final String[] badges) {
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

        int badgeCount = 0;

        for (String badge : this.badges) {
            if (badge != null) {
                badgeCount++;
            }
        }

        msg.writeInt(badgeCount);

        for (int i = 0; i < badges.length; i++) {
            if (badges[i] != null) {
                msg.writeInt(i + 1);
                msg.writeString(badges[i]);
            }
        }
    }
}
