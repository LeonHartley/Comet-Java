package com.cometproject.server.composers.help;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class SanctionStatusComposer extends MessageComposer {

    public SanctionStatusComposer() {

    }

    @Override
    public short getId() {
        return Composers.SanctionStatusMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeString("rocks");
        msg.writeInt(0);//Hours
        msg.writeInt(0);
        msg.writeString("cfh.reason.EMPTY"); // => No sanctions :-)
        msg.writeString("rocks");
        msg.writeInt(0);
        msg.writeString("ALERT"); // => Next sanction = warning
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(false);//if true and second boolean is false it does something. - if false, we got banned, so true is mute
    }
}
