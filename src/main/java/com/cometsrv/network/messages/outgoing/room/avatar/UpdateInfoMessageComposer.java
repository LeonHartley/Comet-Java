package com.cometsrv.network.messages.outgoing.room.avatar;

import com.cometsrv.game.players.data.PlayerData;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class UpdateInfoMessageComposer {
    public static Composer compose(int userId, String figure, String gender, String motto) {
        Composer msg = new Composer(Composers.UpdateInfoMessageComposer);

        msg.writeInt(userId);
        msg.writeString(figure);
        msg.writeString(gender.toLowerCase());
        msg.writeString(motto);
        msg.writeInt(0); // TODO: achiev points

        return msg;
    }

    public static Composer compose(PlayerData data) {
        return compose(data.getId(),data.getFigure(), data.getGender(), data.getMotto());
    }

    public static Composer compose(boolean isMe, PlayerData data) {
        if(!isMe)
            return compose(data.getId(), data.getFigure(), data.getGender(), data.getMotto());
        else
            return compose(-1, data.getFigure(), data.getGender(), data.getMotto());
    }
}
