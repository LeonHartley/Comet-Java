package com.cometproject.server.network.messages.incoming.music;

import com.cometproject.api.game.furniture.types.IMusicData;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.music.SongIdMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class SongIdMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String songName = msg.readString();

        IMusicData musicData = ItemManager.getInstance().getMusicDataByName(songName);

        if (musicData != null) {
            client.send(new SongIdMessageComposer(musicData.getName(), musicData.getSongId()));
        }
    }
}