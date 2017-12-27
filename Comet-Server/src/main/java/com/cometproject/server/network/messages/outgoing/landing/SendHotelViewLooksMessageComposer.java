package com.cometproject.server.network.messages.outgoing.landing;

import com.cometproject.api.game.landing.types.IHallOfFame;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;

public class SendHotelViewLooksMessageComposer extends MessageComposer {

    private final String key;
    private final List<IHallOfFame> players;

    public SendHotelViewLooksMessageComposer(String key, List<IHallOfFame> players) {
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

        for(IHallOfFame player : players) {
            msg.writeInt(player.getId());
            msg.writeString(player.getUsername());
            msg.writeString(player.getFigure());
            msg.writeInt(1);//?
            msg.writeInt(player.getCurrency());
        }
    }
}
