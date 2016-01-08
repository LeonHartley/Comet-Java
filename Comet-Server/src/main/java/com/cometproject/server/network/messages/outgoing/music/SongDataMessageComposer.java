package com.cometproject.server.network.messages.outgoing.music;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.items.music.MusicData;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;

public class SongDataMessageComposer extends MessageComposer {

    private final List<MusicData> musicData;

    public SongDataMessageComposer(List<MusicData> musicData) {
        this.musicData = musicData;
    }

    @Override
    public short getId() {
        return Composers.TraxSongInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(musicData.size());

        for (MusicData musicData : this.musicData) {
            msg.writeInt(musicData.getSongId());
            msg.writeString(musicData.getName());
            msg.writeString(musicData.getTitle());
            msg.writeString(musicData.getData());
            msg.writeInt(musicData.getLengthMilliseconds());
            msg.writeString(musicData.getArtist());
        }
    }
}
