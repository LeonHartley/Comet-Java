package com.cometproject.server.network.messages.outgoing.music.playlist;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.items.music.SongItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.SoundMachineFloorItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;

public class PlaylistMessageComposer extends MessageComposer {
    private List<SongItem> songItems;

    public PlaylistMessageComposer(List<SongItem> songItems) {
        this.songItems = songItems;
    }

    @Override
    public short getId() {
        return Composers.PlaylistMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(SoundMachineFloorItem.MAX_CAPACITY);
        msg.writeInt(songItems.size());

        for (SongItem songItem : this.songItems) {
            msg.writeInt(songItem.getItemSnapshot().getBaseItemId());
            msg.writeInt(songItem.getSongId());
        }
    }
}
