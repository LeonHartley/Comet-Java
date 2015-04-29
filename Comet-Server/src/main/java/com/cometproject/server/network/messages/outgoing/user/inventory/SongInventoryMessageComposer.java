package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.items.music.SongItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

import java.util.List;

public class SongInventoryMessageComposer extends MessageComposer {

    private List<SongItem> songItems;

    public SongInventoryMessageComposer(List<SongItem> songItems) {
        this.songItems = songItems;
    }

    @Override
    public short getId() {
        return Composers.SongInventoryMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.songItems.size());

        for(SongItem songItem : this.songItems) {
            msg.writeInt(songItem.getItemId());
            msg.writeInt(songItem.getSongId());
        }
    }
}
