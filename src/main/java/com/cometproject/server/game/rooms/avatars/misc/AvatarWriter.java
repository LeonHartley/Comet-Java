package com.cometproject.server.game.rooms.avatars.misc;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.RoomEntityType;
import com.cometproject.server.network.messages.types.Composer;

public class AvatarWriter {
    public static void write(GenericEntity entity, Composer msg) {
        msg.writeInt(entity.getVirtualId());
        msg.writeString(entity.getUsername());
        msg.writeString(entity.getMotto());
        msg.writeString(entity.getFigure());
        msg.writeInt(entity.getVirtualId());

        msg.writeInt(entity.getPosition().getX());
        msg.writeInt(entity.getPosition().getY());
        msg.writeDouble(entity.getPosition().getZ());

        boolean isBot = entity.getEntityType() == RoomEntityType.BOT;
        boolean isPet = entity.getEntityType() == RoomEntityType.PET;

        msg.writeInt(isBot ? 4 : 2); // 2 = user 4 = bot
        msg.writeInt(isBot ? 3 : isPet ? 2 : 1); // 1 = user 2 = pet 3 = bot

        msg.writeString(entity.getGender().toLowerCase());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");
        msg.writeString("");
        msg.writeInt(0);
    }
}

