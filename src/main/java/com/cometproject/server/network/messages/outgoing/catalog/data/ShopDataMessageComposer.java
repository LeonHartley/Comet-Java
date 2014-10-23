package com.cometproject.server.network.messages.outgoing.catalog.data;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ShopDataMessageComposer {
    public static Composer compose(int type) {
        Composer msg;

        if (type == 1) {
            msg = new Composer(Composers.ShopData1MessageComposer);
            msg.writeBoolean(true);
            msg.writeInt(1);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(1);
            msg.writeInt(0x2710);
            msg.writeInt(0x30);
            msg.writeInt(7);

        } else {
            msg = new Composer(Composers.ShopData2MessageComposer);

            msg.writeBoolean(true);
            msg.writeInt(1);
            msg.writeInt(10);

            for (int i = 8882; i < 8892; i++) {
                msg.writeInt(i);
            }

            msg.writeInt(8);
            msg.writeInt(0);
            msg.writeInt(1);
            msg.writeInt(2);
            msg.writeInt(3);
            msg.writeInt(4);
            msg.writeInt(5);
            msg.writeInt(6);
            msg.writeInt(8);
            msg.writeInt(11);
            msg.writeInt(0);
            msg.writeInt(1);
            msg.writeInt(2);
            msg.writeInt(3);
            msg.writeInt(4);
            msg.writeInt(5);
            msg.writeInt(6);
            msg.writeInt(7);
            msg.writeInt(8);
            msg.writeInt(9);
            msg.writeInt(10);
            msg.writeInt(7);

            msg.writeInt(204);
            msg.writeInt(205);
            msg.writeInt(206);
            msg.writeInt(207);
            msg.writeInt(208);
            msg.writeInt(209);
            msg.writeInt(210);
        }

        return msg;
    }
}
