package com.cometproject.server.network.messages.outgoing.user.purse;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;

public class CurrenciesMessageComposer {
    public static Composer compose(Map<Integer, Integer> currencies) {
        Composer msg = new Composer(Composers.CurrenciesMessageComposer);

        msg.writeInt(currencies.size());

        for(Map.Entry<Integer, Integer> currency : currencies.entrySet()) {
            msg.writeInt(currency.getKey());
            msg.writeInt(currency.getValue());
        }
/*
        msg.writeInt(11);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(0);
        msg.writeInt(2);
        msg.writeInt(0);
        msg.writeInt(3);
        msg.writeInt(0);
        msg.writeInt(5);
        msg.writeInt(1337);
        msg.writeInt(101);
        msg.writeInt(0);
        msg.writeInt(102);
        msg.writeInt(0);
        msg.writeInt(103);
        msg.writeInt(0);
        msg.writeInt(104);
        msg.writeInt(0);
        msg.writeInt(105);
        msg.writeInt(1337);*/

        return msg;
    }
}
