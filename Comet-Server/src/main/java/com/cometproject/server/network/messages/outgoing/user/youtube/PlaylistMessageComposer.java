package com.cometproject.server.network.messages.outgoing.user.youtube;

import com.cometproject.api.game.players.data.types.IPlaylistItem;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;


public class PlaylistMessageComposer extends MessageComposer {
    private final int itemId;
    private final List<IPlaylistItem> playlist;
    private final int videoId;

    public PlaylistMessageComposer(final int itemId, final List<IPlaylistItem> playlist, final int videoId) {
        this.itemId = itemId;
        this.playlist = playlist;
        this.videoId = videoId;
    }

    @Override
    public short getId() {
        return Composers.YoutubeDisplayPlaylistsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(itemId);

        msg.writeInt(playlist.size());

        for (IPlaylistItem playListItem : playlist) {
            msg.writeString(playlist.indexOf(playListItem)); // not sure if can do this...
            msg.writeString(playListItem.getTitle());
            msg.writeString(playListItem.getDescription());
        }

        msg.writeString(videoId);
    }
}
