package com.cometsrv.network.messages.outgoing.navigator;

import com.cometsrv.game.navigator.types.featured.FeaturedRoom;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

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

            if(room.isCategory()) {
                for(FeaturedRoom room1 : rooms) {
                    if(room1.getCategoryId() == room.getId()) {
                        room1.compose(msg);
                    }
                }
            }
        }

        msg.writeInt(0);
        msg.writeInt(0); // is there a featured featured room? 1= yes, if yes, we need serialize again

        return msg;
    }
}
