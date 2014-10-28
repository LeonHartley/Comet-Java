package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class LoadVolumeSettingsMessageComposer {
    public static Composer compose(Player player) {
        Composer msg = new Composer(Composers.LoadVolumeMessageComposer);

        msg.writeInt(player.getSettings().getVolumes().getSystemVolume());
        msg.writeInt(player.getSettings().getVolumes().getFurniVolume());
        msg.writeInt(player.getSettings().getVolumes().getTraxVolume());
        msg.writeBoolean(player.getSettings().isUseOldChat()); // old chat enabled?
        msg.writeBoolean(true); //something to do with the camera
        msg.writeInt(0); //no idea

        return msg;
    }
}
