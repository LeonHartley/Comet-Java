package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class RoomMuteMessageComposer extends MessageComposer {

    private final boolean roomHasMute;

    public RoomMuteMessageComposer(boolean roomHasMute) {
        this.roomHasMute = roomHasMute;
    }

    @Override
    public short getId() {
        return Composers.RoomMuteSettingsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(this.roomHasMute);
    }
}
