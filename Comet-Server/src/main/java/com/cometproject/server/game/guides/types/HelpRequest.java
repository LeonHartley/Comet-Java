package com.cometproject.server.game.guides.types;

import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Sets;

import java.util.Set;

public class HelpRequest {
    private final int playerId;

    private final int type;
    private final String message;
    public int guideId = -1;
    private boolean recommendation = false;
    private int processTicks = 60;
    private Set<Integer> declinedGuides = Sets.newConcurrentHashSet();

    public HelpRequest(final int playerId, final int type, final String message) {
        this.playerId = playerId;
        this.type = type;
        this.message = message;
    }

    public void decline(final int playerId) {
        this.declinedGuides.add(playerId);
    }

    public boolean declined(final int playerId) {
        return this.declinedGuides.contains(playerId);
    }

    public Session getPlayerSession() {
        return NetworkManager.getInstance().getSessions().getByPlayerId(this.playerId);
    }

    public Session getGuideSession() {
        return this.guideId > 0 ? NetworkManager.getInstance().getSessions().getByPlayerId(this.guideId) : null;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasGuide() {
        return this.guideId > 0;
    }

    public void setGuide(final int guideId) {
        this.guideId = guideId;
    }

    public void incrementProcessTicks() {
        this.processTicks++;
    }

    public void resetProcessTicks() {
        this.processTicks = 0;
    }

    public int getProcessTicks() {
        return this.processTicks;
    }
}
