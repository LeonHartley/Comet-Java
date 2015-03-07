package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class AvatarUpdateMessageComposer extends MessageComposer {

    private final int count;
    private final GenericEntity singleEntity;
    private final List<GenericEntity> entities;

    public AvatarUpdateMessageComposer(final int count, final Collection<GenericEntity> entities) {
        this.count = count;
        this.entities = Lists.newArrayList(entities);
        this.singleEntity = null;
    }

    public AvatarUpdateMessageComposer(final GenericEntity entity) {
        this.count = 1;
        this.singleEntity = entity;
        this.entities = null;
    }

    public AvatarUpdateMessageComposer(final List<GenericEntity> entities) {
        this(entities.size(), Lists.newArrayList(entities));
    }

    @Override
    public short getId() {
        return Composers.AvatarUpdateMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.count);

        if(this.singleEntity != null) {
            this.composeEntity(msg, this.singleEntity);
        } else {
            for(final GenericEntity entity : this.entities) {
                this.composeEntity(msg, entity);
            }
        }
    }

    private void composeEntity(Composer msg, GenericEntity entity) {
        if (!entity.isVisible()) {
            this.cancel();
        }

        msg.writeInt(entity.getId());

        msg.writeInt(entity.getPosition().getX());
        msg.writeInt(entity.getPosition().getY());
        msg.writeString(String.valueOf(entity.getPosition().getZ()));

        msg.writeInt(entity.getHeadRotation());
        msg.writeInt(entity.getBodyRotation());

        StringBuilder statusString = new StringBuilder();
        statusString.append("/");

        for (Map.Entry<RoomEntityStatus, String> status : entity.getStatuses().entrySet()) {

            statusString.append(status.getKey().getStatusCode());

            if (!status.getValue().isEmpty()) {
                statusString.append(" ");
                statusString.append(status.getValue());
            }

            statusString.append("/");
        }

        statusString.append("/");

        msg.writeString(statusString.toString());
    }

    @Override
    public void dispose() {
        if(this.entities != null) {
            this.entities.clear();
        }
    }
}
