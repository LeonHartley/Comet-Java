package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.game.players.components.types.VolumeData;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.google.gson.Gson;

public class UpdateAudioSettingsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if (client.getPlayer() == null) { return; }

        int vol1 = msg.readInt();
        int vol2 = msg.readInt();
        int vol3 = msg.readInt();

        if (client.getPlayer().getSettings().getVolumes().getSystemVolume() == vol1
                && client.getPlayer().getSettings().getVolumes().getFurniVolume() == vol2
                && client.getPlayer().getSettings().getVolumes().getTraxVolume() == vol3) {
            return;
        }

        PlayerDao.saveVolume(new Gson().toJson(new VolumeData(vol1, vol2, vol3)), client.getPlayer().getId());
    }
}
