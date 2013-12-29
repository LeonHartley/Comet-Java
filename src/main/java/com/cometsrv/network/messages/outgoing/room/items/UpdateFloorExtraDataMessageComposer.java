package com.cometsrv.network.messages.outgoing.room.items;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class UpdateFloorExtraDataMessageComposer {
    public static Composer compose(int id, String data) {
        Composer msg = new Composer(Composers.UpdateFloorExtraDataMessageComposer);

        msg.writeString(id);
        msg.writeInt(0);
        msg.writeString(data);
        
        return msg;
    }
}
