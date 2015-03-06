package com.cometproject.server.network.messages.outgoing.user.youtube;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


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
        return Composers.YouTubeLoadVideoMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(itemId);
        msg.writeString(videoId);
        msg.writeInt(0);
        msg.writeInt(videoLength);
    }
}
