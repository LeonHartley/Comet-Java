package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class AchievementPointsMessageComposer {
    public static Composer compose(int points) {
        Composer msg = new Composer(Composers.AchievementPointsMessageComposer);

        msg.writeInt(points);

        return msg;
    }
}
