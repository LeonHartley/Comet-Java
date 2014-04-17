package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.items.data.MannequinData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class UpdateFloorExtraDataMessageComposer {
    public static Composer compose(int id, String data) {
        Composer msg = new Composer(Composers.UpdateFloorExtraDataMessageComposer);

        if (data.contains(";#;")) {
            msg.writeString(String.valueOf(id));

            msg.writeInt(1);
            msg.writeInt(3);

            MannequinData mannequinData = MannequinData.get(data);

            msg.writeString("FIGURE");
            msg.writeString(mannequinData.getFigure());
            msg.writeString("GENDER");
            msg.writeString(mannequinData.getGender());
            msg.writeString("OUTFIT_NAME");
            msg.writeString(mannequinData.getName());

        } else {
            msg.writeString(id);
            msg.writeInt(0);
            msg.writeString(data);
        }

        return msg;
    }
}
