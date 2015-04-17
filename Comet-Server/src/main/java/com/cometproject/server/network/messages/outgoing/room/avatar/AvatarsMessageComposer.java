package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class AvatarsMessageComposer extends MessageComposer {
    private final RoomInstance room;
    private final GenericEntity singleEntity;

    private AvatarsMessageComposer(final RoomInstance room, final GenericEntity singleEntity) {
        this.room = room;
        this.singleEntity = singleEntity;
    }

    public AvatarsMessageComposer(final RoomInstance room) {
        this(room, null);
    }

    public AvatarsMessageComposer(final GenericEntity singleEntity) {
        this(null, singleEntity);
    }

    @Override
    public short getId() {
        return Composers.SetRoomUserMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        if(this.singleEntity != null) {
            msg.writeInt(1);

            this.singleEntity.compose(msg);
        } else {
            msg.writeInt(room.getEntities().count());

            for (GenericEntity entity : room.getEntities().getAllEntities().values()) {
                if (!entity.isVisible()) continue;

                entity.compose(msg);
            }
        }
    }
}
