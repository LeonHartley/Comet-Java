package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class AvatarUpdateMessageComposer extends MessageComposer {

    private int count;
    private AvatarState singleEntity;
    private List<AvatarState> entities;

    public AvatarUpdateMessageComposer(final Collection<GenericEntity> entities) {
        this.entities = Lists.newArrayList();

        for (GenericEntity entity : entities) {
            if (!entity.isVisible()) {
                continue;
            }

            this.entities.add(new AvatarState(entity));
        }

        this.count = this.entities.size();
        this.singleEntity = null;
    }

    public AvatarUpdateMessageComposer(final GenericEntity entity) {
        this.count = 1;
        this.singleEntity = new AvatarState(entity);
        this.entities = null;
    }

    @Override
    public short getId() {
        return Composers.AvatarUpdateMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.count);

        if (this.singleEntity != null) {
            this.composeEntity(msg, this.singleEntity);
        } else {
            for (final AvatarState entity : this.entities) {
                this.composeEntity(msg, entity);
            }
        }
    }

    private void composeEntity(IComposer msg, AvatarState entity) {
        msg.writeInt(entity.getId());

        msg.writeInt(entity.getPosition().getX());
        msg.writeInt(entity.getPosition().getY());
        msg.writeString(String.valueOf(entity.getPosition().getZ()));

        msg.writeInt(entity.getHeadRotation());
        msg.writeInt(entity.getBodyRotation());

        msg.writeString(entity.getStatusString());
    }

    private class AvatarState {
        private int id;
        private Position position;
        private int headRotation;
        private int bodyRotation;
        private String statusString;

        public AvatarState(GenericEntity entity) {
            this.id = entity.getId();
            this.position = entity.getPosition().copy();
            this.headRotation = entity.getHeadRotation();
            this.bodyRotation = entity.getBodyRotation();

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

            this.statusString = statusString.toString();
        }

        public int getId() {
            return id;
        }

        public Position getPosition() {
            return position;
        }

        public int getHeadRotation() {
            return headRotation;
        }

        public int getBodyRotation() {
            return bodyRotation;
        }

        public String getStatusString() {
            return statusString;
        }

        @Override
        public String toString() {
            return String.format("AvatatState[%s, %s, %s, %s, %s]", this.id, this.position, this.headRotation, this.bodyRotation, this.statusString);
        }
    }
}
