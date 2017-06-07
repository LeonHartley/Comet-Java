package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class UserBadgesMessageComposer extends MessageComposer {
    private final int playerId;
    private final List<String> badges;

    public UserBadgesMessageComposer(final int playerId, final List<String> badges) {
        this.playerId = playerId;
        this.badges = new LinkedList<>(badges);
    }

    @Override
    public short getId() {
        return Composers.HabboUserBadgesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(playerId);
        msg.writeInt(badges.size());

        int counter = 0;

        for(String badge : this.badges) {
            msg.writeInt(counter++);
            msg.writeString(badge);
        }
    }
}
