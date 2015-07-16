package com.cometproject.server.network.messages.outgoing.music;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class PlayMusicMessageComposer extends MessageComposer {

    private int songId;
    private int playlistIndex;
    private int timestamp;

    public PlayMusicMessageComposer(int songId, int playlistIndex, int timestamp) {
        this.songId = songId;
        this.playlistIndex = playlistIndex;
        this.timestamp = timestamp;
    }

    public PlayMusicMessageComposer() {
        this.songId = -1;
    }

    @Override
    public short getId() {
        return Composers.PlayMusicMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (this.songId == -1) {
            msg.writeInt(-1);
            msg.writeInt(-1);
            msg.writeInt(-1);
            msg.writeInt(-1);
            msg.writeInt(0);
            return;
        }

        msg.writeInt(songId);
        msg.writeInt(playlistIndex);
        msg.writeInt(songId);
        msg.writeInt(0);
        msg.writeInt(timestamp);
    }
}
