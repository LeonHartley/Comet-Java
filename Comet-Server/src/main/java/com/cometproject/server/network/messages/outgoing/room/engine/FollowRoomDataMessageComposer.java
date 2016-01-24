package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class FollowRoomDataMessageComposer extends MessageComposer {
    private final RoomData roomData;
    private final boolean isLoading;
    private final boolean checkEntry;
    private final boolean skipAuth;
    private final boolean canMute;

    public FollowRoomDataMessageComposer(final RoomData room, boolean isLoading, boolean checkEntry, boolean skipAuth, boolean canMute) {
        this.roomData = room;
        this.isLoading = isLoading;
        this.checkEntry = checkEntry;
        this.skipAuth = skipAuth;
        this.canMute = canMute;
    }

    @Override
    public short getId() {
        return Composers.GetGuestRoomResultMessageComposer;
    }

    @Override
    public void compose(IComposer composer) {
        RoomWriter.entryData(this.roomData, composer, this.isLoading, this.checkEntry, this.skipAuth, this.canMute);
    }
}
