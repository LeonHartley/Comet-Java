package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.components.types.MessengerRequest;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class UpdateFriendStateMessageComposer {
    public static Composer compose(MessengerRequest request, boolean online, boolean inRoom) {
        return compose(request.getFromId(), request.getUsername(), request.getLook(), request.getMotto(), online, inRoom);
    }

    public static Composer compose(PlayerData player, boolean online, boolean inRoom) {
        return compose(player.getId(), player.getUsername(), player.getFigure(), player.getMotto(), online, inRoom);
    }

    public static Composer compose(int id, String username, String figure, String motto, boolean online, boolean inRoom) {
        Composer msg = new Composer(Composers.UpdateFriendStateMessageComposer);

        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(0);
        msg.writeInt(id);
        msg.writeString(username);
        msg.writeInt(1);
        msg.writeBoolean(online);
        msg.writeBoolean(inRoom);
        msg.writeString(figure);
        msg.writeInt(0);
        msg.writeString(motto);
        msg.writeString(""); // additional name ?
        msg.writeString("");
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeBoolean(false);

        return msg;
    }
}
