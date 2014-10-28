package com.cometproject.server.network.messages.outgoing.room.poll;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ShowRoomPollMessageComposer {
    public static Composer compose(int roomId) {
        Composer msg = new Composer(Composers.PollQuestionsMessageComposer);

        msg.writeInt(1);
        msg.writeString("Do you like Comet?");
        msg.writeString("test test test");
        msg.writeInt(0);
        msg.writeInt(roomId);
        msg.writeInt(0);
        msg.writeInt(0);


        return msg;
    }
}
