package com.cometproject.server.game.guides;

import com.cometproject.server.game.guides.types.HelperSession;
import com.cometproject.server.utilities.Initialisable;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GuideManager implements Initialisable {
    private static GuideManager guideManagerInstance;

    private final Map<Integer, HelperSession> sessions = new ConcurrentHashMap<>();

    private Set<Integer> activeGuides = new ConcurrentHashSet<>();
    private Set<Integer> activeGuardians = new ConcurrentHashSet<>();

    @Override
    public void initialize() {

    }

    public void startPlayerDuty(final HelperSession helperSession) {
        this.sessions.put(helperSession.getPlayerId(), helperSession);

        if(helperSession.handlesHelpRequests()) {
            this.activeGuides.add(helperSession.getPlayerId());
        }

        if(helperSession.handlesBullyReports()) {
            this.activeGuardians.add(helperSession.getPlayerId());
        }
    }

    public void finishPlayerDuty(final HelperSession helperSession) {
        //check if they have any on-going stuff?

        if(this.sessions.containsKey(helperSession.getPlayerId())) {
            this.sessions.remove(helperSession.getPlayerId());
        }

        if(helperSession.handlesHelpRequests()) {
            this.activeGuides.remove(helperSession.getPlayerId());
        }

        if(helperSession.handlesBullyReports()) {
            this.activeGuardians.remove(helperSession.getPlayerId());
        }
    }

    public int getActiveGuideCount() {
        return this.activeGuides.size();
    }

    public int getActiveGuardianCount() {
        return this.activeGuardians.size();
    }

    public static GuideManager getInstance() {
        if(guideManagerInstance == null) {
            guideManagerInstance = new GuideManager();
        }

        return guideManagerInstance;
    }
}
