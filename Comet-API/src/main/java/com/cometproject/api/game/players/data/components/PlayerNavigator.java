package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.players.data.components.navigator.ISavedSearch;

public interface PlayerNavigator {
    boolean isSearchSaved(ISavedSearch savedSearch);
}
