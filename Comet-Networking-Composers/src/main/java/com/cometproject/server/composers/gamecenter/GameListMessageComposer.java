package com.cometproject.server.composers.gamecenter;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class GameListMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.GameListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(1);

        msg.writeInt(3);
        msg.writeString("basejump");
        msg.writeString("68bbd2");
        msg.writeString("ffffff");
        msg.writeString("http://localhost/comet/swf/games/gamecenter_basejump/");
        msg.writeString("");
    }
}
