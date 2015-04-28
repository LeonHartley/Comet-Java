package com.cometproject.server.network.messages.outgoing.user.youtube;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.components.types.settings.PlaylistItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class PlaylistMessageComposer extends MessageComposer {
    private final int itemId;
    private final List<PlaylistItem> playlist;
    private final int videoId;

    public PlaylistMessageComposer(final int itemId, final List<PlaylistItem> playlist, final int videoId) {
        this.itemId = itemId;
        this.playlist = playlist;
        this.videoId = videoId;
    }

    @Override
    public short getId() {
        return Composers.YouTubeLoadPlaylistsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(itemId);

        msg.writeInt(playlist.size());

        for (PlaylistItem playListItem : playlist) {
            msg.writeString(playlist.indexOf(playListItem)); // not sure if can do this...
            msg.writeString(playListItem.getTitle());
            msg.writeString(playListItem.getDescription());
        }

        msg.writeString(videoId);
    }
}
