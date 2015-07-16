package com.cometproject.server.network.messages.incoming.music;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.music.MusicData;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.music.SongIdMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class SongIdMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String songName = msg.readString();

        MusicData musicData = ItemManager.getInstance().getMusicDataByName(songName);

        if (musicData != null) {
            client.send(new SongIdMessageComposer(musicData.getName(), musicData.getSongId()));
        }
    }
}