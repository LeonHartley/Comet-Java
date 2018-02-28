package com.cometproject.server.network.messages.outgoing.room.polls;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class QuickPollMessageComposer extends MessageComposer {

    private final String question;

    public QuickPollMessageComposer(String question) {
        this.question = question;
    }

    @Override
    public short getId() {
        return Composers.QuickPollMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("");
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(1);//duration
        msg.writeInt(-1);//id
        msg.writeInt(120);//number
        msg.writeInt(3);
        msg.writeString(this.question);
    }
}
