package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.game.navigator.types.featured.FeaturedRoom;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class FeaturedRoomsMessageComposer {
    public static Composer compose(List<FeaturedRoom> rooms) {
        Composer msg = new Composer(Composers.FeaturedRoomsMessageComposer);

        msg.writeInt(rooms.size());

        for(FeaturedRoom room : rooms) {
            if(!room.isCategory()) {
                continue;
            }

            room.compose(msg);

            for(FeaturedRoom room1 : rooms) {
                if(room1.getCategoryId() != room.getId())
                    continue;

                room1.compose(msg);
            }
        }

        for(FeaturedRoom room : rooms) {
            if(!room.isCategory() && room.isRecommended()) {
                msg.writeInt(1);
                room.compose(msg);
                msg.writeInt(0);

                return msg;
            }
        }

        msg.writeInt(0);
        msg.writeInt(0);
        return msg;
    }
}
