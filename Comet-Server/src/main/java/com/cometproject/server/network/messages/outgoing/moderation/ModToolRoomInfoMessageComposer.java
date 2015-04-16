package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class ModToolRoomInfoMessageComposer extends MessageComposer {
    private final RoomInstance room;

    public ModToolRoomInfoMessageComposer(final RoomInstance room) {
        this.room = room;
    }

    @Override
    public short getId() {
        return Composers.ModerationRoomToolMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
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

        msg.writeBoolean(false); // has event
    }
}
