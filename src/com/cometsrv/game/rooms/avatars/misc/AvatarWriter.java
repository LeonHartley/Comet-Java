package com.cometsrv.game.rooms.avatars.misc;


import com.cometsrv.game.players.data.PlayerData;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.network.messages.types.Composer;

public class AvatarWriter {
    public static void write(Avatar avatar, Composer msg) {
        PlayerData data = avatar.getPlayer().getData();

        msg.writeInt(data.getId());
        msg.writeString(data.getUsername());
        msg.writeString(data.getMotto());
        msg.writeString(data.getFigure());
        msg.writeInt(data.getId());

        msg.writeInt(avatar.getPosition().getX());
        msg.writeInt(avatar.getPosition().getY());
        msg.writeDouble(avatar.getPosition().getZ());

        msg.writeInt(2); // 2 = user 4 = bot
        msg.writeInt(1); // 1 = user 2 = pet 3 = bot

        msg.writeString(data.getGender().toLowerCase());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");
        msg.writeString("");
        msg.writeInt(0);
    }
}

