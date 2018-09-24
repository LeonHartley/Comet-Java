package com.cometproject.api.game.players.data.components.messenger;

import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.networking.sessions.ISession;
import com.google.gson.JsonObject;

public interface IMessengerFriend {
    boolean isInRoom();

    PlayerAvatar getAvatar();

    int getUserId();

    boolean isOnline();

    ISession getSession();

    JsonObject toJson();
}
