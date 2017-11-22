package com.cometproject.api.game.players.data.components.messenger;

import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.networking.sessions.ISession;

public interface IMessengerFriend {
    boolean isInRoom();

    PlayerAvatar getAvatar();

    int getUserId();

    boolean isOnline();

    ISession getSession();
}
