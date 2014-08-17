package com.cometproject.server.game.items.rares;

import com.cometproject.server.storage.queries.items.LimitedEditionDao;
import javolution.util.FastMap;

public class LimitedEditionManager {

    private FastMap<Integer, LimitedEditionItem> limitedEditionItems;

    public LimitedEditionManager() {
        this.limitedEditionItems = new FastMap<>();
    }

    public LimitedEditionItem getLimitedEdition(int itemId) {
        if (this.limitedEditionItems.containsKey(itemId)) {
            return this.limitedEditionItems.get(itemId);
        }

        LimitedEditionItem item = LimitedEditionDao.get(itemId);

        if (item != null) {
            this.limitedEditionItems.put(itemId, item);
        }

        return null;
    }
}
