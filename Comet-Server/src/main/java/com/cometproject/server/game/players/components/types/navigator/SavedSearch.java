package com.cometproject.server.game.players.components.types.navigator;

public class SavedSearch {
    private final String view;
    private final String searchQuery;

    public SavedSearch(final String view, final String searchQuery) {
        this.view = view;
        this.searchQuery = searchQuery;
    }

    public String getView() {
        return view;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
