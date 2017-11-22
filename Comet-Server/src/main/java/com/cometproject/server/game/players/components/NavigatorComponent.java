package com.cometproject.server.game.players.components;

import com.cometproject.api.game.players.data.components.navigator.ISavedSearch;
import com.cometproject.server.game.players.components.types.navigator.SavedSearch;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.Map;
import java.util.Set;

public class NavigatorComponent extends PlayerComponent {

    private final Map<Integer, ISavedSearch> savedSearches;
    private final Set<Integer> favouriteRooms;

    public NavigatorComponent(final Player player) {
        super(player);

        this.savedSearches = PlayerDao.getSavedSearches(this.getPlayer().getId());
        this.favouriteRooms = PlayerDao.getFavouriteRooms(this.getPlayer().getId());
    }

    public void dispose() {
        this.savedSearches.clear();
        this.favouriteRooms.clear();
    }

    public boolean isSearchSaved(SavedSearch newSearch) {
        for(ISavedSearch savedSearch : this.getSavedSearches().values()) {
            if(savedSearch.getView().equals(newSearch.getView()) && savedSearch.getSearchQuery().equals(newSearch.getSearchQuery()))
                return true;
        }

        return false;
    }

    public Set<Integer> getFavouriteRooms() {
        return this.favouriteRooms;
    }

    public Map<Integer, ISavedSearch> getSavedSearches() {
        return this.savedSearches;
    }
}
