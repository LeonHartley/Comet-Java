package com.cometproject.server.network.messages.outgoing.room.filter;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Set;

public class GetRoomFilterListMessageComposer extends MessageComposer {

    private final Set<String> filter;

    public GetRoomFilterListMessageComposer(final Set<String> filter) {
        this.filter = filter;
    }

    @Override
    public short getId() {
        return Composers.GetRoomFilterListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(filter.size());

        for (String word : this.filter) {
            msg.writeString(word);
        }
    }
}
