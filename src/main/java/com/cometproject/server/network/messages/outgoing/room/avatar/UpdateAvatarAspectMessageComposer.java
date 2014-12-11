package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class UpdateAvatarAspectMessageComposer {
    public static Composer compose(String figure, String gender) {
        Composer msg = new Composer(Composers.UpdateAvatarAspectMessageComposer);

        msg.writeString(figure);
        msg.writeString(gender);

        return msg;
    }
}
