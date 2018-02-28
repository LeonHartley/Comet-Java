package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class ModToolRoomInfoMessageComposer extends MessageComposer {
    private final Room room;

    public ModToolRoomInfoMessageComposer(final Room room) {
        this.room = room;
    }

    @Override
    public short getId() {
        return Composers.ModeratorRoomInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(room.getId());
        msg.writeInt(room.getEntities().playerCount());

        msg.writeBoolean(PlayerManager.getInstance().isOnline(room.getData().getOwnerId()));
        msg.writeInt(room.getData().getOwnerId());
        msg.writeString(room.getData().getOwner());

        msg.writeBoolean(true); // TODO: Allow for rooms that aren't active to show here ;-)

        msg.writeString(room.getData().getName());
        msg.writeString(room.getData().getDescription());
        msg.writeInt(room.getData().getTags().length);

        for (int i = 0; i < room.getData().getTags().length; i++) {
            msg.writeString(room.getData().getTags()[i]);
        }
    }
}
