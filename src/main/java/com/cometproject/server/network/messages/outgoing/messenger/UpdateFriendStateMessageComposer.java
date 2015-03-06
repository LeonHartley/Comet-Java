package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class UpdateFriendStateMessageComposer extends MessageComposer {
    private final PlayerAvatar playerAvatar;
    private final boolean online;
    private final boolean inRoom;

    public UpdateFriendStateMessageComposer(final PlayerAvatar playerAvatar, final boolean online, final boolean inRoom) {
        this.playerAvatar = playerAvatar;
        this.online = online;
        this.inRoom = inRoom;
    }

    @Override
    public short getId() {
        return Composers.FriendUpdateMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(0);
        msg.writeInt(this.playerAvatar.getId());
        msg.writeString(this.playerAvatar.getFigure());
        msg.writeInt(1);
        msg.writeBoolean(online);
        msg.writeBoolean(inRoom);
        msg.writeString(this.playerAvatar.getFigure());
        msg.writeInt(0);
        msg.writeString(this.playerAvatar.getMotto());
        msg.writeString(""); // facebook name ?
        msg.writeString("");
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeBoolean(false);
    }
}
