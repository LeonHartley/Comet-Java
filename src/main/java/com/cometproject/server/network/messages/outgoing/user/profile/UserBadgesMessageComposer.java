package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;

public class UserBadgesMessageComposer {
    public static Composer compose(Map<String, Integer> badges) {
        Composer msg = new Composer(Composers.UpdateBadgesMessageComposer);
    }
}
