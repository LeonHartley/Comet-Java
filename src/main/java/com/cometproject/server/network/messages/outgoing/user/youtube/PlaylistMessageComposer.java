package com.cometproject.server.network.messages.outgoing.user.youtube;

import com.cometproject.server.game.players.components.types.PlaylistItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class PlaylistMessageComposer {
    public static Composer compose(int itemId, List<PlaylistItem> playlist, int videoId) {
        Composer msg = new Composer(Composers.PlaylistMessageComposer);

        msg.writeInt(itemId);

        msg.writeInt(playlist.size());

        for(PlaylistItem playListItem : playlist) {
            msg.writeString(playlist.indexOf(playListItem)); // not sure if can do this...
            msg.writeString(playListItem.getTitle());
            msg.writeString(playListItem.getDescription());
        }

        if(playlist.size() > 0) {
            msg.writeString(videoId);
        }

        return msg;
    }
}
