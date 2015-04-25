package com.cometproject.server.game.items;

import com.cometproject.server.game.items.music.MusicData;
import com.cometproject.server.game.items.storage.ItemStorageQueue;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.items.MusicDao;
import com.cometproject.server.storage.queries.items.TeleporterDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.utilities.Initializable;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import javax.jws.soap.SOAPBinding;


public class ItemManager implements Initializable {
    private static ItemManager itemManagerInstance;

    private Logger log = Logger.getLogger(ItemManager.class.getName());

    private FastMap<Integer, ItemDefinition> itemDefinitions;
    private FastMap<Integer, Integer> itemSpriteIdToDefinitionId;

    private FastMap<Integer, MusicData> musicData;

    public ItemManager() {

    }

    @Override
    public void initialize() {
        this.itemDefinitions = new FastMap<>();
        this.musicData = new FastMap<>();

        this.loadItemDefinitions();
        this.loadMusicData();

        ItemStorageQueue.getInstance().initialize();

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

    public void loadMusicData() {
        if(!this.musicData.isEmpty()) {
            this.musicData.clear();
        }

        MusicDao.getMusicData(this.musicData);
        log.info("Loaded " + this.musicData.size() + " songs");
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

//        log.debug("Couldn't find item definition for item: " + itemId + ", make sure the database is complete! (`furniture` table)");

        return null;
    }

    public MusicData getMusicData(int songId) {
        if(this.musicData.containsKey(songId)) {
            return this.musicData.get(songId);
        }

        return null;
    }

    public ItemDefinition getBySpriteId(int spriteId) {
        return this.itemDefinitions.get(this.itemSpriteIdToDefinitionId.get(spriteId));
    }

    public FastMap<Integer, ItemDefinition> getItemDefinitions() {
        return this.itemDefinitions;
    }
}
