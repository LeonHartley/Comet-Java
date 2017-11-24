package com.cometproject.api.game.moderation.guides;

import com.cometproject.api.networking.sessions.ISession;

public interface IHelpRequest {
    void decline(int playerId);

    boolean declined(int playerId);

    ISession getPlayerSession();

    ISession getGuideSession();

    int getPlayerId();

    int getType();

    String getMessage();

    boolean hasGuide();

    void setGuide(int guideId);

    void incrementProcessTicks();

    void resetProcessTicks();

    int getProcessTicks();
}
