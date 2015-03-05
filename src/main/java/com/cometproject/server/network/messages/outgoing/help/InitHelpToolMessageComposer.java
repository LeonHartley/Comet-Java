package com.cometproject.server.network.messages.outgoing.help;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class InitHelpToolMessageComposer extends MessageComposer {
    public InitHelpToolMessageComposer() {

    }

    @Override
    public short getId() {
        return Composers.InitHelpToolMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(0); // TODO: code this.
    }
}
