package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class AchievementPointsMessageComposer extends MessageComposer {
    private final int points;

    public AchievementPointsMessageComposer(final int points) {
        this.points = points;
    }

    @Override
    public short getId() {
        return Composers.AchievementScoreMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.points);
    }
}
