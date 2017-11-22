package com.cometproject.server.game.players.components.types.navigator;

import com.cometproject.api.game.players.data.components.navigator.ISavedSearch;

public class SavedSearch implements ISavedSearch {
    private final String view;
    private final String searchQuery;

    public SavedSearch(final String view, final String searchQuery) {
        this.view = view;
        this.searchQuery = searchQuery;
    }

    @Override
    public String getView() {
        return view;
    }

    @Override
    public String getSearchQuery() {
        return searchQuery;
    }
}
