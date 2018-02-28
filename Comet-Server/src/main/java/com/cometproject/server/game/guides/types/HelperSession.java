package com.cometproject.server.game.guides.types;

public class HelperSession {
    public HelpRequest handlingRequest;
    private int playerId;
    private boolean tourRequests;
    private boolean helpRequests;
    private boolean bullyReports;

    public HelperSession(int playerId, boolean tourRequests, boolean helpRequests, boolean bullyReports) {
        this.playerId = playerId;

        this.tourRequests = tourRequests;
        this.helpRequests = helpRequests;
        this.bullyReports = bullyReports;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public boolean handlesTourRequests() {
        return tourRequests;
    }

    public boolean handlesHelpRequests() {
        return helpRequests;
    }

    public boolean handlesBullyReports() {
        return bullyReports;
    }
}
