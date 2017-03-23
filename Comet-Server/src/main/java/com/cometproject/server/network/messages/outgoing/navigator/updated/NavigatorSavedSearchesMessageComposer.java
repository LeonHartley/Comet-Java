package com.cometproject.server.network.messages.outgoing.navigator.updated;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

/**
 * Created by Leon on 23/03/2017.
 */
public class NavigatorSavedSearchesMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.NavigatorSavedSearchesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(0);//count

//        msg.writeInt(1);
//        msg.writeString("a");
//        msg.writeString("b");
//        msg.writeString("c");
    }
}
