package com.cometproject.server.network.messages.outgoing.landing;

import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Map;

public class SendHotelViewLooksMessageComposer extends MessageComposer {

    private final String key;
    private final Map<PlayerAvatar, Integer> players;

    public SendHotelViewLooksMessageComposer(String key, Map<PlayerAvatar, Integer> players) {
        this.key = key;
        this.players = players;
    }

    @Override
    public short getId() {
        return Composers.SendHotelViewLooksMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(key);
        msg.writeInt(this.players.size());

        for (Map.Entry<PlayerAvatar, Integer> player : players.entrySet()) {
            msg.writeInt(player.getKey().getId());
            msg.writeString(player.getKey().getUsername());
            msg.writeString(player.getKey().getFigure());
            msg.writeInt(1);//?
            msg.writeInt(player.getValue());
        }

    }
}
