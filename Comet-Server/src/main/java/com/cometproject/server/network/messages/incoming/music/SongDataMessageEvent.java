package com.cometproject.server.network.messages.incoming.music;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.music.MusicData;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.music.SongDataMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.List;

public class SongDataMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int size = msg.readInt();

        List<MusicData> musicDataList = Lists.newArrayList();

        for (int i = 0; i < size; i++) {
            int songId = msg.readInt();

            MusicData musicData = ItemManager.getInstance().getMusicData(songId);

            if (musicData != null) {
                musicDataList.add(musicData);
            }
        }

        if (!musicDataList.isEmpty())
            client.send(new SongDataMessageComposer(musicDataList));
    }
}
