package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Map;


public class PopularTagsMessageComposer extends MessageComposer {
    private final Map<String, Integer> popularTags;

    public PopularTagsMessageComposer(final Map<String, Integer> popularTags) {
        this.popularTags = popularTags;
    }

    @Override
    public short getId() {
        return Composers.PopularRoomTagsResultMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(popularTags.size() > 50 ? 50 : popularTags.size());

        for (Map.Entry<String, Integer> entry : popularTags.entrySet()) {
            msg.writeString(entry.getKey());
            msg.writeInt(entry.getValue());
        }
    }

    @Override
    public void dispose() {
        this.popularTags.clear();
    }
}
