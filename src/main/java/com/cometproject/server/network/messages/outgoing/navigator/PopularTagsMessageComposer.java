package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PopularTagsMessageComposer {
    public static Composer compose(Map<String, Integer> popularTags) {
        Composer msg = new Composer(Composers.PopularRoomTagsMessageComposer);

        msg.writeInt(popularTags.size() > 50 ? 50 : popularTags.size());

        for(Map.Entry<String, Integer> entry : popularTags.entrySet()) {
            msg.writeString(entry.getKey());
            msg.writeInt(entry.getValue());
        }

        return msg;
    }
}
