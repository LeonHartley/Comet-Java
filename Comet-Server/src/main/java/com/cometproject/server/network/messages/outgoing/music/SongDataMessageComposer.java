package com.cometproject.server.network.messages.outgoing.music;

import com.cometproject.api.game.furniture.types.IMusicData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;

public class SongDataMessageComposer extends MessageComposer {

    private final List<IMusicData> musicData;

    public SongDataMessageComposer(List<IMusicData> musicData) {
        this.musicData = musicData;
    }

    @Override
    public short getId() {
        return Composers.SongDataMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(musicData.size());

        for (IMusicData musicData : this.musicData) {
            msg.writeInt(musicData.getSongId());
            msg.writeString(musicData.getName());
            msg.writeString(musicData.getTitle());
            msg.writeString(musicData.getData());
            msg.writeInt(musicData.getLengthMilliseconds());
            msg.writeString(musicData.getArtist());
        }
    }
}
