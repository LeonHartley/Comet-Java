package com.cometproject.server.game.players.components;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.music.SongItem;
import com.cometproject.server.game.items.rares.LimitedEditionItem;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.wired.WiredRewardMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BadgeInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.RemoveObjectFromInventoryMessageComposer;
import com.cometproject.server.storage.queries.achievements.PlayerAchievementDao;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class InventoryComponent implements PlayerComponent {
    private Player player;

    private Map<Long, InventoryItem> floorItems;
    private Map<Long, InventoryItem> wallItems;
    private Map<String, Integer> badges;

    private boolean itemsLoaded = false;

    private Logger log = Logger.getLogger(InventoryComponent.class.getName());

    public InventoryComponent(Player player) {
        this.player = player;

        this.floorItems = new ConcurrentHashMap<>();
        this.wallItems = new ConcurrentHashMap<>();
        this.badges = new ConcurrentHashMap<>();

        this.loadBadges();
    }

    public void loadItems() {
        this.itemsLoaded = true;

        if (this.getWallItems().size() >= 1) {
            this.getWallItems().clear();
        }
        if (this.getFloorItems().size() >= 1) {
            this.getFloorItems().clear();
        }

        try {
            Map<Long, InventoryItem> inventoryItems = InventoryDao.getInventoryByPlayerId(this.player.getId());

            for (Map.Entry<Long, InventoryItem> item : inventoryItems.entrySet()) {
                if (item.getValue().getDefinition().getType().equals("s")) {
                    this.getFloorItems().put(item.getKey(), item.getValue());
                }

                if (item.getValue().getDefinition().getType().equals("i")) {
                    this.getWallItems().put(item.getKey(), item.getValue());
                }
            }
        } catch (Exception e) {
            log.error("Error while loading user inventory", e);
        }
    }

    public void loadBadges() {
        try {
            // TODO: redo this so we can seperate achievement badges to other badges. Maybe a "badge type" or something.
            this.badges = InventoryDao.getBadgesByPlayerId(player.getId());
        } catch (Exception e) {
            log.error("Error while loading user badges");
        }
    }

    public void addBadge(String code, boolean insert) {
        this.addBadge(code, insert, true);
    }

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

    public boolean hasBadge(String code) {
        return this.badges.containsKey(code);
    }

    public void removeBadge(String code, boolean delete) {
        this.removeBadge(code, delete, true, true);
    }

    public void removebadge(String code, boolean delete, boolean sendAlert) {
        this.removeBadge(code, delete, sendAlert, true);
    }

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

    public void resetBadgeSlots() {
        for (Map.Entry<String, Integer> badge : this.badges.entrySet()) {
            if (badge.getValue() != 0) {
                this.badges.replace(badge.getKey(), 0);
            }
        }
    }

    public Map<String, Integer> equippedBadges() {
        Map<String, Integer> badges = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> badge : this.getBadges().entrySet()) {
            if (badge.getValue() > 0)
                badges.put(badge.getKey(), badge.getValue());
        }

        return badges;
    }

    public InventoryItem add(long id, int itemId, String extraData, GiftData giftData, LimitedEditionItem limitedEditionItem) {
        InventoryItem item = new InventoryItem(id, itemId, extraData, giftData, limitedEditionItem);

        if (item.getDefinition().getType().equals("s")) {
            this.getFloorItems().put(id, item);
        }

        if (item.getDefinition().getType().equals("i")) {
            this.getWallItems().put(id, item);
        }

        return item;
    }

    public List<SongItem> getSongs() {
        List<SongItem> songItems = Lists.newArrayList();

        for (InventoryItem inventoryItem : this.floorItems.values()) {
            if (inventoryItem.getDefinition().isSong()) {
                songItems.add(new SongItem(inventoryItem.createSnapshot(), inventoryItem.getDefinition().getSongId()));
            }
        }

        return songItems;
    }

    public void add(long id, int itemId, String extraData, LimitedEditionItem limitedEditionItem) {
        add(id, itemId, extraData, null, limitedEditionItem);
    }

    public void addItem(InventoryItem item) {
        if ((this.floorItems.size() + this.wallItems.size()) >= 5000) {
            this.getPlayer().sendNotif("Notice", Locale.getOrDefault("game.inventory.limitExceeded", "You have over 5,000 items in your inventory. The next time you login, you will only see the first 5000 items."));
        }

        if (item.getDefinition().getType().equals("s"))
            floorItems.put(item.getId(), item);
        else if (item.getDefinition().getType().equals("i"))
            wallItems.put(item.getId(), item);
    }

    public void removeItem(InventoryItem item) {
        if (item.getDefinition().getType().equals("s"))
            floorItems.remove(item.getId());
        else if (item.getDefinition().getType().equals("i"))
            wallItems.remove(item.getId());
    }

    public void removeFloorItem(long itemId) {
        if (this.getFloorItems() == null) {
            return;
        }

        this.getFloorItems().remove(itemId);
        this.getPlayer().getSession().send(new RemoveObjectFromInventoryMessageComposer(ItemManager.getInstance().getItemVirtualId(itemId)));
    }

    public void removeWallItem(long itemId) {
        this.getWallItems().remove(itemId);
        this.getPlayer().getSession().send(new RemoveObjectFromInventoryMessageComposer(ItemManager.getInstance().getItemVirtualId(itemId)));
    }

    public boolean hasFloorItem(long id) {
        return this.getFloorItems().containsKey(id);
    }

    public InventoryItem getFloorItem(long id) {
        if (!this.hasFloorItem(id)) {
            return null;
        }
        return this.getFloorItems().get(id);
    }

    public boolean hasWallItem(long id) {
        return this.getWallItems().containsKey(id);
    }

    @Deprecated
    public InventoryItem getWallItem(int id) {
        return getWallItem(ItemManager.getInstance().getItemIdByVirtualId(id));
    }

    @Deprecated
    public InventoryItem getFloorItem(int id) {
        return getFloorItem(ItemManager.getInstance().getItemIdByVirtualId(id));
    }

    public InventoryItem getWallItem(long id) {
        if (!this.hasWallItem(id)) {
            return null;
        }
        return this.getWallItems().get(id);
    }

    public InventoryItem getItem(long id) {
        InventoryItem item = getFloorItem(id);

        if (item != null) {
            return item;
        }

        return getWallItem(id);
    }

    public void dispose() {
        this.floorItems.clear();
        this.floorItems = null;

        this.wallItems.clear();
        this.wallItems = null;

        this.badges.clear();
        this.badges = null;
    }

    public int getTotalSize() {
        return this.getWallItems().size() + this.getFloorItems().size();
    }

    public Map<Long, InventoryItem> getWallItems() {
        return this.wallItems;
    }

    public Map<Long, InventoryItem> getFloorItems() {
        return this.floorItems;
    }

    public Map<String, Integer> getBadges() {
        return this.badges;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean itemsLoaded() {
        return itemsLoaded;
    }
}
