package com.cometproject.server.network.messages.incoming.user.youtube;

import com.cometproject.server.game.players.components.types.PlaylistItem;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.youtube.PlayVideoMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.youtube.PlaylistMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.RandomInteger;

import java.util.List;

public class LoadPlaylistMessageEvent implements IEvent{

    @Override
    public void handle(Session client, Event msg) throws Exception {
        int itemId = msg.readInt();

        List<PlaylistItem> playlist = client.getPlayer().getSettings().getPlaylist();

        int randomId = RandomInteger.getRandom(0, playlist.size() - 1);

        client.send(PlaylistMessageComposer.compose(itemId, playlist, randomId));

        if(playlist.size() > 0) {
            PlaylistItem video = playlist.get(randomId);
            client.send(PlayVideoMessageComposer.compose(itemId, video.getVideoId(), video.getDuration()));

            FloorItem item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

            item.setAttribute("video", video.getVideoId());
            item.sendUpdate();
        }
    }
}
