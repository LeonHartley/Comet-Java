package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.game.players.components.types.VolumeData;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.utilities.JsonFactory;


public class UpdateAudioSettingsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if (client.getPlayer() == null) {
            return;
        }

        int systemVolume = msg.readInt();
        int furniVolume = msg.readInt();
        int traxVolume = msg.readInt();

        if (client.getPlayer().getSettings().getVolumes().getSystemVolume() == systemVolume
                && client.getPlayer().getSettings().getVolumes().getFurniVolume() == furniVolume
                && client.getPlayer().getSettings().getVolumes().getTraxVolume() == traxVolume) {
            return;
        }

        PlayerDao.saveVolume(JsonFactory.getInstance().toJson(new VolumeData(systemVolume, furniVolume, traxVolume)), client.getPlayer().getId());
    }
}
