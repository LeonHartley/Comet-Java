package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Set;


public class FavouriteRoomsMessageComposer extends MessageComposer {
    private static final int MAX_FAVOURITE_ROOMS = 30;

    private final Set<Integer> favouriteRooms;

    public FavouriteRoomsMessageComposer(final Set<Integer> favouriteRooms) {
        this.favouriteRooms = favouriteRooms;
    }

    @Override
    public short getId() {
        return Composers.FavouritesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(MAX_FAVOURITE_ROOMS);
        msg.writeInt(this.favouriteRooms.size());//size

        for(int roomId : this.favouriteRooms) {
            msg.writeInt(roomId);
        }
    }
}
