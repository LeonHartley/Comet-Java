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

        return msg;
    }
}
