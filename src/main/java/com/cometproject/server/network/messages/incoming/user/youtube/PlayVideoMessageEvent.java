package com.cometproject.server.network.messages.incoming.user.youtube;

import com.cometproject.server.game.players.components.types.PlaylistItem;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.youtube.PlayVideoMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PlayVideoMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) throws Exception {
        int itemId = msg.readInt();
        int videoId = msg.readInt();

        PlaylistItem playlistItem = client.getPlayer().getSettings().getPlaylist().get(videoId);

        client.send(PlayVideoMessageComposer.compose(itemId, playlistItem.getVideoId(), playlistItem.getDuration()));
        RoomItemFloor item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

        item.setAttribute("video", playlistItem.getVideoId());

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(item, client.getPlayer().getEntity().getRoom().getData().getOwnerId()));
    }
}
