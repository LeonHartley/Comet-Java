package com.cometproject.server.network.messages.outgoing.user.camera;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class CameraTokenMessageComposer {
    public static Composer compose(String token) {
        Composer msg = new Composer(Composers.CameraTokenMessageComposer);

        msg.writeString(token);

        return msg;
    }
}
