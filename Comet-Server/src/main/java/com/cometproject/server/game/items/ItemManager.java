package com.cometproject.server.game.items;

import com.cometproject.api.game.furniture.IFurnitureService;
import com.cometproject.api.game.furniture.types.CrackableReward;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.IMusicData;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.items.MusicDao;
import com.cometproject.server.storage.queries.items.TeleporterDao;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.api.data.Data;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ItemManager implements IFurnitureService {
    private static ItemManager itemManagerInstance;

    private Logger log = Logger.getLogger(ItemManager.class.getName());

    private Map<Integer, FurnitureDefinition> itemDefinitions;

    private Map<Integer, Integer> itemSpriteIdToDefinitionId;
    private Map<Integer, IMusicData> musicData;

    private Map<Long, Integer> itemIdToVirtualId;
    private Map<Integer, Long> virtualIdToItemId;
    private Map<Integer, CrackableReward> crackableRewards;

    private AtomicInteger itemIdCounter;
    private Integer saddleId;

    public ItemManager() {

    }

    public static ItemManager getInstance() {
        if (itemManagerInstance == null) {
            itemManagerInstance = new ItemManager();
        }

        return itemManagerInstance;
    }

    @Override
    public void initialize() {
        this.itemDefinitions = new HashMap<>();
        this.musicData = new HashMap<>();

        this.itemIdToVirtualId = new ConcurrentHashMap<>();
        this.virtualIdToItemId = new ConcurrentHashMap<>();
        this.crackableRewards = new ConcurrentHashMap<>();

        this.itemIdCounter = new AtomicInteger(1);

        this.loadItemDefinitions();
        this.loadMusicData();

        log.info("ItemManager initialized");
    }

    @Override
    public void loadItemDefinitions() {
        Map<Integer, FurnitureDefinition> tempMap = this.itemDefinitions;
        Map<Integer, Integer> tempSpriteIdItemMap = this.itemSpriteIdToDefinitionId;

        try {
            this.crackableRewards.clear();

            this.itemDefinitions = ItemDao.getDefinitions();
            this.crackableRewards = ItemDao.getCrackableRewards();
            this.itemSpriteIdToDefinitionId = new HashMap<>();
        } catch (Exception e) {
            log.error("Error while loading item definitions", e);
        }

        if (tempMap.size() >= 1) {
            tempMap.clear();
            tempSpriteIdItemMap.clear();
        }

        if (this.itemDefinitions != null) {
            for (FurnitureDefinition itemDefinition : this.itemDefinitions.values()) {
                if (itemDefinition.getItemName().equals("horse_saddle1")) {
                    this.saddleId = itemDefinition.getId();
                }

                this.itemSpriteIdToDefinitionId.put(itemDefinition.getSpriteId(), itemDefinition.getId());
            }
        }

        log.info("Loaded " + this.getItemDefinitions().size() + " item definitions");
    }

    @Override
    public void loadMusicData() {
        if (!this.musicData.isEmpty()) {
            this.musicData.clear();
        }

        MusicDao.getMusicData(this.musicData);
        log.info("Loaded " + this.musicData.size() + " songs");
    }

    @Override
    public int getItemVirtualId(long itemId) {
        if (this.itemIdToVirtualId.containsKey(itemId)) {
            return this.itemIdToVirtualId.get(itemId);
        }

        int virtualId = this.itemIdCounter.getAndIncrement();

        this.itemIdToVirtualId.put(itemId, virtualId);
        this.virtualIdToItemId.put(virtualId, itemId);

        return virtualId;
    }

    @Override
    public void disposeItemVirtualId(long itemId) {
        int virtualId = this.getItemVirtualId(itemId);

        this.itemIdToVirtualId.remove(itemId);
        this.virtualIdToItemId.remove(virtualId);
    }

    @Override
    public Long getItemIdByVirtualId(int virtualId) {
        return this.virtualIdToItemId.get(virtualId);
    }

    @Override
    public long getTeleportPartner(long itemId) {
        return TeleporterDao.getPairId(itemId);
    }

    @Override
    public int roomIdByItemId(long itemId) {
        final Data<Integer> data = Data.createEmpty();

        StorageContext.getCurrentContext().getRoomItemRepository().getRoomIdByItemId(itemId, data::set);

        if (data.has()) {
            return data.get();
        }

        return 0;
    }

    @Override
    public FurnitureDefinition getDefinition(int itemId) {
        if (this.getItemDefinitions().containsKey(itemId)) {
            return this.getItemDefinitions().get(itemId);
        }

        return null;
    }

    @Override
    public IMusicData getMusicData(int songId) {
        if (this.musicData.containsKey(songId)) {
            return this.musicData.get(songId);
        }

        return null;
    }

    @Override
    public IMusicData getMusicDataByName(String name) {
        for (IMusicData musicData : this.musicData.values()) {
            if (musicData.getName().equals(name)) {
                return musicData;
            }
        }

        return null;
    }

    @Override
    public Map<Long, Integer> getItemIdToVirtualIds() {
        return itemIdToVirtualId;
    }

    @Override
    public FurnitureDefinition getBySpriteId(int spriteId) {
        return this.itemDefinitions.get(this.itemSpriteIdToDefinitionId.get(spriteId));
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public Map<Integer, FurnitureDefinition> getItemDefinitions() {
        return this.itemDefinitions;
    }

    @Override
    public Integer getSaddleId() {
        return saddleId;
    }

    public Map<Integer, CrackableReward> getCrackableRewards() {
        return crackableRewards;
    }
}
