package com.cometproject.server.game.items;

import com.cometproject.server.game.items.rares.LimitedEditionItem;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.items.TeleporterDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.utilities.Initializable;
import javolution.util.FastMap;
import org.apache.log4j.Logger;


public class ItemManager implements Initializable {
    private static ItemManager itemManagerInstance;

    private Logger log = Logger.getLogger(ItemManager.class.getName());

    private FastMap<Integer, ItemDefinition> itemDefinitions;
    private FastMap<Integer, Integer> itemSpriteIdToDefinitionId;
    private FastMap<Integer, LimitedEditionItem> limitedEditionItems;

    public ItemManager() {

    }

    @Override
    public void initialize() {
        this.itemDefinitions = new FastMap<>();
        this.limitedEditionItems = new FastMap<>();

        this.loadItemDefinitions();

        log.info("ItemManager initialized");
    }

    public static ItemManager getInstance() {
        if (itemManagerInstance == null) {
            itemManagerInstance = new ItemManager();
        }

        return itemManagerInstance;
    }

    public void loadItemDefinitions() {
        FastMap<Integer, ItemDefinition> tempMap = this.itemDefinitions;
        FastMap<Integer, Integer> tempSpriteIdItemMap = this.itemSpriteIdToDefinitionId;
        
        try {
            this.itemDefinitions = ItemDao.getDefinitions();
            this.itemSpriteIdToDefinitionId = new FastMap<>();
        } catch (Exception e) {
            log.error("Error while loading item definitions", e);
        }

        if (tempMap.size() >= 1) {
            tempMap.clear();
            tempSpriteIdItemMap.clear();
        }

        if (this.itemDefinitions != null) {
            for (ItemDefinition itemDefinition : this.itemDefinitions.values()) {
                this.itemSpriteIdToDefinitionId.put(itemDefinition.getSpriteId(), itemDefinition.getId());
            }
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

    public ItemDefinition getBySpriteId(int spriteId) {
        return this.itemDefinitions.get(this.itemSpriteIdToDefinitionId.get(spriteId));
    }

    public LimitedEditionItem getLimitedEdition(int itemId) {
        if (this.limitedEditionItems.containsKey(itemId)) {
            if(this.limitedEditionItems.get(itemId) == LimitedEditionItem.NONE) {
                return null;
            }

            return this.limitedEditionItems.get(itemId);
        }

        // TODO: LRU cache
//        LimitedEditionItem item = LimitedEditionDao.get(itemId);

//        if (item != null) {
//            this.limitedEditionItems.put(itemId, item);
//        } else {
//            this.limitedEditionItems.put(itemId, LimitedEditionItem.NONE);
//            return null;
//        }

        return null;
    }

    public FastMap<Integer, ItemDefinition> getItemDefinitions() {
        return this.itemDefinitions;

    }
}
