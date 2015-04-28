package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class LogoutMessageComposer extends MessageComposer {
    private final int reason;

    public LogoutMessageComposer(final String reason) {
        if(reason.equals("banned")) {
            this.reason = 1;
        } else {
            this.reason = 0;
        }
    }

    @Override
    public short getId() {
        return Composers.LogoutMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.reason);
    }
}
