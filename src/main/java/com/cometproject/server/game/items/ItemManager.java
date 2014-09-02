package com.cometproject.server.game.items;

import com.cometproject.server.game.items.rares.LimitedEditionManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.items.TeleporterDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

public class ItemManager {
    private Logger log = Logger.getLogger(ItemManager.class.getName());

    private FastMap<Integer, ItemDefinition> itemDefinitions;

    private LimitedEditionManager limitedEditionManager;

    public ItemManager() {
        this.itemDefinitions = new FastMap<>();
        this.limitedEditionManager = new LimitedEditionManager();

        this.loadItemDefinitions();
    }

    public void loadItemDefinitions() {
        if (this.getItemDefinitions().size() >= 1) {
            this.getItemDefinitions().clear();
        }

        try {
            this.itemDefinitions = ItemDao.getDefinitions();
        } catch (Exception e) {
            log.error("Error while loading item definitions", e);
        }

        log.info("Loaded " + this.getItemDefinitions().size() + " item definitions");
    }

    public int getTeleportPartner(int itemId) {
        return TeleporterDao.getPairId(itemId);
    }

    public int roomIdByItemId(int itemId) {
        return RoomItemDao.getRoomIdById(itemId);
    }

    public ItemDefinition getDefinition(int itemId) {
        if (this.getItemDefinitions().containsKey(itemId)) {
            return this.getItemDefinitions().get(itemId);
        }

        log.debug("Couldn't find item definition for item: " + itemId + ", make sure the database is complete! (`furniture` table)");

        return null;
    }

    public FastMap<Integer, ItemDefinition> getItemDefinitions() {
        return this.itemDefinitions;
    }

    public LimitedEditionManager getLimitedEditionManager() {
        return limitedEditionManager;
    }
}
