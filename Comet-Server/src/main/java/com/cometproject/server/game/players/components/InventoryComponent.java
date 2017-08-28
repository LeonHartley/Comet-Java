package com.cometproject.server.game.players.components;

import com.cometproject.api.game.furniture.types.GiftItemData;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.furniture.types.SongItem;
import com.cometproject.api.game.players.data.components.PlayerInventory;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.items.music.SongItemData;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.players.components.types.inventory.InventoryItemSnapshot;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.wired.WiredRewardMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BadgeInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.InventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.RemoveObjectFromInventoryMessageComposer;
import com.cometproject.server.storage.queries.achievements.PlayerAchievementDao;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class InventoryComponent implements PlayerInventory {
    private Player player;

    private Map<Long, PlayerItem> inventoryItems;

    private Map<String, Integer> badges;

    private boolean itemsLoaded = false;

    private Logger log = Logger.getLogger(InventoryComponent.class.getName());

    public InventoryComponent(Player player) {
        this.player = player;

        this.inventoryItems = new ConcurrentHashMap<>();

        this.badges = new ConcurrentHashMap<>();

        this.loadBadges();
    }

    @Override
    public void loadItems() {
        this.itemsLoaded = true;

        if(this.inventoryItems.size() >= 1) {
            this.inventoryItems.clear();
        }

        try {
            Map<Long, PlayerItem> inventoryItems = InventoryDao.getInventoryByPlayerId(this.player.getId());

            for (Map.Entry<Long, PlayerItem> item : inventoryItems.entrySet()) {
                this.inventoryItems.put(item.getKey(), item.getValue());
            }
        } catch (Exception e) {
            log.error("Error while loading user inventory", e);
        }
    }

    @Override
    public void loadBadges() {
        try {
            // TODO: redo this so we can seperate achievement badges to other badges. Maybe a "badge type" or something.
            this.badges = InventoryDao.getBadgesByPlayerId(player.getId());
        } catch (Exception e) {
            log.error("Error while loading user badges");
        }
    }

    @Override
    public void addBadge(String code, boolean insert) {
        this.addBadge(code, insert, true);
    }

    @Override
    public void addBadge(String code, boolean insert, boolean sendAlert) {
        if (!badges.containsKey(code)) {
            if (insert) {
                InventoryDao.addBadge(code, this.player.getId());
            }

            this.badges.put(code, 0);

            this.player.getSession().
                    send(new BadgeInventoryMessageComposer(this.getBadges())).
                    send(new UnseenItemsMessageComposer(new HashMap<Integer, List<Integer>>() {{
                        put(4, Lists.newArrayList(1));
                    }}));
            
            if (sendAlert) {
                this.player.getSession().send(new WiredRewardMessageComposer(7));
            }

        }
    }

    public void send() {
        if(this.inventoryItems.size() == 0) {
            this.player.getSession().send(new InventoryMessageComposer(1, 0, Maps.newHashMap()));
            return;
        }

        int totalPages = (int) Math.ceil(this.inventoryItems.size() / InventoryMessageComposer.ITEMS_PER_PAGE);

        int totalSent = 0;
        int currentPage = 0;
        Map<Long, PlayerItem> inventoryItems = new HashMap<>();

        for(Map.Entry<Long, PlayerItem> item : this.getInventoryItems().entrySet()) {
            totalSent++;
            inventoryItems.put(item.getKey(), item.getValue());

            if(inventoryItems.size() >= InventoryMessageComposer.ITEMS_PER_PAGE || totalSent == this.inventoryItems.size()) {
                this.player.getSession().send(new InventoryMessageComposer(totalPages + 1, currentPage, inventoryItems));

                inventoryItems = new HashMap<>();
                currentPage++;
            }
        }
    }

    @Override
    public boolean hasBadge(String code) {
        return this.badges.containsKey(code);
    }

    @Override
    public void removeBadge(String code, boolean delete) {
        this.removeBadge(code, delete, true, true);
    }

    @Override
    public void removebadge(String code, boolean delete, boolean sendAlert) {
        this.removeBadge(code, delete, sendAlert, true);
    }

    @Override
    public void removeBadge(String code, boolean delete, boolean sendAlert, boolean sendUpdate) {
        if (badges.containsKey(code)) {
            if (delete) {
                InventoryDao.removeBadge(code, player.getId());
            }

            this.badges.remove(code);

            if (sendAlert) {
                this.player.getSession().send(new AlertMessageComposer(Locale.get("badge.deleted")));
            }

            this.player.getSession().send(new BadgeInventoryMessageComposer(this.badges));
        }
    }

    @Override
    public void achievementBadge(String achievement, int level) {
        final String oldBadge = achievement + (level - 1);
        final String newBadge = achievement + level;

        boolean isUpdated = false;

        if (this.badges.containsKey(oldBadge)) {
            this.removeBadge(oldBadge, false, false, false);

            PlayerAchievementDao.updateBadge(oldBadge, newBadge, this.player.getId());
            isUpdated = true;
        }

        this.addBadge(newBadge, !isUpdated, false);
    }

    @Override
    public void resetBadgeSlots() {
        for (Map.Entry<String, Integer> badge : this.badges.entrySet()) {
            if (badge.getValue() != 0) {
                this.badges.replace(badge.getKey(), 0);
            }
        }
    }

    @Override
    public Map<String, Integer> equippedBadges() {
        Map<String, Integer> badges = new ConcurrentHashMap<>();

        for (Map.Entry<String, Integer> badge : this.getBadges().entrySet()) {
            if (badge.getValue() > 0)
                badges.put(badge.getKey(), badge.getValue());
        }

        return badges;
    }

    @Override
    public PlayerItem add(long id, int itemId, String extraData, GiftItemData giftData, LimitedEditionItem limitedEditionItem) {
        PlayerItem item = new InventoryItem(id, itemId, extraData, giftData, limitedEditionItem);

        this.inventoryItems.put(id, item);
        return item;
    }

    @Override
    public List<SongItem> getSongs() {
        List<SongItem> songItems = Lists.newArrayList();

        for (PlayerItem inventoryItem : this.inventoryItems.values()) {
            if (inventoryItem.getDefinition().isSong()) {
                songItems.add(new SongItemData((InventoryItemSnapshot) inventoryItem.createSnapshot(), inventoryItem.getDefinition().getSongId()));
            }
        }

        return songItems;
    }

    @Override
    public void add(long id, int itemId, String extraData, LimitedEditionItem limitedEditionItem) {
        add(id, itemId, extraData, null, limitedEditionItem);
    }

    @Override
    public void addItem(PlayerItem item) {
        this.inventoryItems.put(item.getId(), item);
    }

    @Override
    public void removeItem(PlayerItem item) {
        this.removeItem(item.getId());
    }

    @Override
    public void removeItem(long itemId) {
        this.inventoryItems.remove(itemId);
        this.getPlayer().getSession().send(new RemoveObjectFromInventoryMessageComposer(ItemManager.getInstance().getItemVirtualId(itemId)));
    }

    @Override
    public boolean hasItem(long id) {
        return this.getInventoryItems().containsKey(id);
    }

    @Override
    public PlayerItem getItem(long id) {
        return this.inventoryItems.get(id);
    }

    @Override
    public void dispose() {
        for(PlayerItem inventoryItem : this.inventoryItems.values()) {
            ItemManager.getInstance().disposeItemVirtualId(inventoryItem.getId());
        }

        this.inventoryItems.clear();
        this.inventoryItems = null;

        this.badges.clear();
        this.badges = null;
    }

    @Override
    public int getTotalSize() {
        return this.inventoryItems.size();
    }

    @Override
    public Map<Long, PlayerItem> getInventoryItems() {
        return this.inventoryItems;
    }

    @Override
    public Map<String, Integer> getBadges() {
        return this.badges;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean itemsLoaded() {
        return itemsLoaded;
    }
}
