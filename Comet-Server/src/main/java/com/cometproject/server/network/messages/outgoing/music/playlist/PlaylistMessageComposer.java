package com.cometproject.server.network.messages.outgoing.music.playlist;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.items.music.SongItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.SoundMachineFloorItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;

public class PlaylistMessageComposer extends MessageComposer {
    private List<SongItemData> songItemDatas;

    public PlaylistMessageComposer(List<SongItemData> songItemDatas) {
        this.songItemDatas = songItemDatas;
    }

    @Override
    public short getId() {
        return Composers.PlaylistMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(SoundMachineFloorItem.MAX_CAPACITY);
        msg.writeInt(songItemDatas.size());

        for (SongItemData songItemData : this.songItemDatas) {
            msg.writeInt(songItemData.getItemSnapshot().getBaseItemId());
            msg.writeInt(songItemData.getSongId());
        }
    }
}
