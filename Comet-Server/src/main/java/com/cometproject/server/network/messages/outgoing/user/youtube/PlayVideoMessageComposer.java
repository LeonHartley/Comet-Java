package com.cometproject.server.network.messages.outgoing.user.youtube;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class PlayVideoMessageComposer extends MessageComposer {
    private final int itemId;
    private final String videoId;
    private final int videoLength;

    public PlayVideoMessageComposer(final int itemId, final String videoId, final int videoLength) {
        this.itemId = itemId;
        this.videoId = videoId;
        this.videoLength = videoLength;
    }

    @Override
    public short getId() {
        return Composers.YouTubeDisplayVideoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(itemId);
        msg.writeString(videoId);
        msg.writeInt(0);
        msg.writeInt(videoLength);
        msg.writeInt(0);
    }
}
