package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.google.common.collect.Lists;

import java.util.List;


public class AvatarsMessageComposer extends MessageComposer {
    private final GenericEntity singleEntity;
    private final List<GenericEntity> entities;

    public AvatarsMessageComposer(final Room room) {
        this.entities = Lists.newArrayList();

        for (GenericEntity entity : room.getEntities().getAllEntities().values()) {
            if(entity.isVisible()) {
                if(entity instanceof PlayerEntity) {
                    if(((PlayerEntity) entity).getPlayer() == null) continue;
                }

                this.entities.add(entity);
            }
        }

        this.singleEntity = null;
    }

    public AvatarsMessageComposer(GenericEntity entity) {
        this.singleEntity = entity;
        this.entities = null;
    }

    public AvatarsMessageComposer(List<GenericEntity> entities) {
        this.singleEntity = null;
        this.entities = entities;
    }

    @Override
    public short getId() {
        return Composers.SetRoomUserMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (this.singleEntity != null) {
            msg.writeInt(1);

            this.singleEntity.compose(msg);
        } else {
            msg.writeInt(this.entities.size());
            for(GenericEntity entity : this.entities) {
                entity.compose(msg);
            }
        }
    }
}
