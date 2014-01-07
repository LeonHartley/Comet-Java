package com.cometsrv.network.messages.outgoing.room.avatar;

import com.cometsrv.game.rooms.entities.GenericEntity;
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

    public static Composer compose(GenericEntity entity) {
        return compose(entity.getVirtualId(), entity.getFigure(), entity.getGender(), entity.getMotto());
    }

    public static Composer compose(boolean isMe, GenericEntity entity) {
        if(!isMe) {
            return compose(entity.getVirtualId(), entity.getFigure(), entity.getGender(), entity.getMotto());
        } else {
            return compose(-1, entity.getFigure(), entity.getGender(), entity.getMotto());
        }
    }
}
