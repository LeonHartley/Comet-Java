package com.cometproject.server.game.guides;

import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.game.guides.types.HelpRequest;
import com.cometproject.server.game.guides.types.HelperSession;
import com.cometproject.server.network.messages.outgoing.help.guides.GuideSessionAttachedMessageComposer;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class GuideManager implements Initialisable {
    private static GuideManager guideManagerInstance;

    private final Map<Integer, HelperSession> sessions = new ConcurrentHashMap<>();

    private final Map<Integer, Boolean> activeGuides = new ConcurrentHashMap<>();
    private final Set<Integer> activeGuardians = new ConcurrentHashSet<>();

    private final Map<Integer, HelpRequest> activeHelpRequests = new ConcurrentHashMap<>();

    public static GuideManager getInstance() {
        if (guideManagerInstance == null) {
            guideManagerInstance = new GuideManager();
        }

        return guideManagerInstance;
    }

    @Override
    public void initialize() {
        CometThreadManager.getInstance().executePeriodic(this::processRequests, 1000L, 1000L, TimeUnit.MILLISECONDS);
    }

    private void processRequests() {
        // Loop through every request and make sure it has an attached guide, if it doesn't.. find a guide that hasn't
        // declined it yet.

        for (HelpRequest helpRequest : this.activeHelpRequests.values()) {
            if (!helpRequest.hasGuide()) {
                if (helpRequest.getProcessTicks() >= 60) {
                    // Find a guide!
                    for (Map.Entry<Integer, Boolean> activeGuide : activeGuides.entrySet()) {
                        if (!activeGuide.getValue()) { // Guide is available!
                            if (!helpRequest.declined(activeGuide.getKey())) {
                                helpRequest.setGuide(activeGuide.getKey());

                                helpRequest.getPlayerSession().send(new GuideSessionAttachedMessageComposer(helpRequest, false));
                                helpRequest.getGuideSession().send(new GuideSessionAttachedMessageComposer(helpRequest, true));
                                break;
                            }
                        }
                    }

                    if (helpRequest.hasGuide()) {
                        this.activeGuides.put(helpRequest.guideId, true);
                    }

                    // None found? Search again!
                    helpRequest.resetProcessTicks();
                } else {
                    helpRequest.incrementProcessTicks();
                }
            }
        }
    }

    public void startPlayerDuty(final HelperSession helperSession) {
        this.sessions.put(helperSession.getPlayerId(), helperSession);

        if (helperSession.handlesHelpRequests()) {
            this.activeGuides.put(helperSession.getPlayerId(), false);
        }

        if (helperSession.handlesBullyReports()) {
            this.activeGuardians.add(helperSession.getPlayerId());
        }
    }

    public void finishPlayerDuty(final HelperSession helperSession) {
        //check if they have any on-going stuff?

        this.sessions.remove(helperSession.getPlayerId());

        if (helperSession.handlesHelpRequests()) {
            this.activeGuides.remove(helperSession.getPlayerId());
        }

        if (helperSession.handlesBullyReports()) {
            this.activeGuardians.remove(helperSession.getPlayerId());
        }
    }

    public void requestHelp(final HelpRequest helpRequest) {
        this.activeHelpRequests.put(helpRequest.getPlayerId(), helpRequest);
    }

    public HelpRequest getHelpRequestByCreator(final int playerId) {
        return this.activeHelpRequests.get(playerId);
    }

    public int getActiveGuideCount() {
        return this.activeGuides.size();
    }

    public int getActiveGuardianCount() {
        return this.activeGuardians.size();
    }
}
