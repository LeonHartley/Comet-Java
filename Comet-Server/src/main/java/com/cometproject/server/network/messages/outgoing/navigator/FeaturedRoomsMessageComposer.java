package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.navigator.types.featured.FeaturedRoom;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Collection;


public class FeaturedRoomsMessageComposer extends MessageComposer {
    private final Collection<FeaturedRoom> featuredRooms;

    public FeaturedRoomsMessageComposer(Collection<FeaturedRoom> featuredRooms) {
        this.featuredRooms = featuredRooms;
    }

    @Override
    public short getId() {
        return 0;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.featuredRooms.size());

        for (FeaturedRoom room : this.featuredRooms) {
            if (room.getCategoryId() > 0)
                continue;

            room.compose(msg);

            for (FeaturedRoom room1 : this.featuredRooms) {
                if (room1.getCategoryId() != room.getId()) {
                    continue;
                }

                room1.compose(msg);
            }
        }

        for (FeaturedRoom room : this.featuredRooms) {
            if (!room.isCategory() && room.isRecommended()) {
                msg.writeInt(1);
                room.compose(msg);
                msg.writeInt(0);

                return;
            }
        }

        msg.writeInt(0);
        msg.writeInt(0);
    }
}
