package com.cometproject.server.network.messages.outgoing.room.items.postit;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class PostItMessageComposer extends MessageComposer {
    private final int id;
    private final String data;

    public PostItMessageComposer(final int id, final String data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public short getId() {
        return Composers.StickyNoteMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.id + "");
        msg.writeString(this.data);
    }
}
