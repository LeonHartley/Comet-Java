package com.cometsrv.network.messages.outgoing.room.avatar;

import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.components.bots.Bot;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

import java.util.List;
import java.util.Map;

public class AvatarUpdateMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.RoomStatusesMessageComposer);

        msg.writeInt(room.getEntities().count()); // items count

        for(GenericEntity entity : room.getEntities().getEntitiesCollection().values()) {
            msg.writeInt(entity.getVirtualId());

            msg.writeInt(entity.getPosition().getX());
            msg.writeInt(entity.getPosition().getY());
            msg.writeString(String.valueOf(entity.getPosition().getZ()));

            msg.writeInt(entity.getHeadRotation());
            msg.writeInt(entity.getBodyRotation());

            StringBuilder statusString = new StringBuilder();
            statusString.append("/");

            for(Map.Entry<String, String> status : entity.getStatuses().entrySet()) {

                statusString.append(status.getKey());

                if(!status.getValue().isEmpty()) {
                    statusString.append(" ");
                    statusString.append(status.getValue());
                }

                statusString.append("/");
            }

            statusString.append("/");

            msg.writeString(statusString.toString());
        }

        return msg;
    }

    public static Composer compose(List<GenericEntity> list) {
        Composer msg = new Composer(Composers.RoomStatusesMessageComposer);

        msg.writeInt(list.size()); // items count

        for(GenericEntity entity : list) {
            msg.writeInt(entity.getVirtualId());

            msg.writeInt(entity.getPosition().getX());
            msg.writeInt(entity.getPosition().getY());
            msg.writeString(String.valueOf(entity.getPosition().getZ()));

            msg.writeInt(entity.getHeadRotation());
            msg.writeInt(entity.getBodyRotation());

            StringBuilder statusString = new StringBuilder();
            statusString.append("/");

            for(Map.Entry<String, String> status : entity.getStatuses().entrySet()) {

                statusString.append(status.getKey());

                if(!status.getValue().isEmpty()) {
                    statusString.append(" ");
                    statusString.append(status.getValue());
                }

                statusString.append("/");
            }

            statusString.append("/");

            msg.writeString(statusString.toString());
        }

        return msg;
    }

    public static Composer compose(GenericEntity entity) {
        Composer msg = new Composer(Composers.RoomStatusesMessageComposer);

        msg.writeInt(1);

        msg.writeInt(entity.getVirtualId());

        msg.writeInt(entity.getPosition().getX());
        msg.writeInt(entity.getPosition().getY());
        msg.writeString(String.valueOf(entity.getPosition().getZ()));

        msg.writeInt(entity.getHeadRotation());
        msg.writeInt(entity.getBodyRotation());

        StringBuilder statusString = new StringBuilder();
        statusString.append("/");

        for(Map.Entry<String, String> status : entity.getStatuses().entrySet()) {

            statusString.append(status.getKey());

            if(!status.getValue().isEmpty()) {
                statusString.append(" ");
                statusString.append(status.getValue());
            }

            statusString.append("/");
        }

        statusString.append("/");

        msg.writeString(statusString.toString());

        return msg;
    }

    public static Composer compose(Bot avatar) {
        Composer msg = new Composer(Composers.RoomStatusesMessageComposer);

        msg.writeInt(1);

        msg.writeInt(avatar.getVirtualId());

        msg.writeInt(avatar.getX());
        msg.writeInt(avatar.getY());
        msg.writeString(String.valueOf(avatar.getZ()));

        msg.writeInt(avatar.getHeadRotation());
        msg.writeInt(avatar.getBodyRotation());

        StringBuilder statusString = new StringBuilder();
        statusString.append("/");

        for(Map.Entry<String, String> status : avatar.getStatuses().entrySet()) {

            statusString.append(status.getKey());

            if(!status.getValue().isEmpty()) {
                statusString.append(" ");
                statusString.append(status.getValue());
            }

            statusString.append("/");
        }

        statusString.append("/");

        msg.writeString(statusString.toString());

        return msg;
    }
}
