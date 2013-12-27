package com.cometsrv.network.messages.outgoing.catalog.data;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class ShopDataMessageComposer {
    public static Composer compose(int type) {
        Composer msg;

        if(type == 1) {
            msg = new Composer(Composers.ShopData1MessageComposer);
            msg.writeBoolean(true);
            msg.writeInt(1);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(1);
            msg.writeInt(10000);
            msg.writeInt(48);
            msg.writeInt(7);

        } else {
            msg = new Composer(Composers.ShopData2MessageComposer);

            msg.writeBoolean(true);
            msg.writeInt(1);
            msg.writeInt(10);

            for(int i = 3372; i < 3382; i++) {
                msg.writeInt(i);
            }

            msg.writeInt(7);
            msg.writeInt(0);
            msg.writeInt(1);
            msg.writeInt(2);
            msg.writeInt(3);
            msg.writeInt(4);
            msg.writeInt(5);
            msg.writeInt(6);
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

            for(int i = 187; i < 194; i++) {
                msg.writeInt(i);
            }
        }

        return msg;
    }
}
