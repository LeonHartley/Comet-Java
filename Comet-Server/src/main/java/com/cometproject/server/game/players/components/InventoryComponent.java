package com.cometproject.server.game.players.components;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.music.SongItem;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.game.rooms.objects.items.types.floor.SoundMachineFloorItem;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.wired.WiredRewardMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BadgeInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.RemoveObjectFromInventoryMessageComposer;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;
import com.google.common.collect.Lists;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InventoryComponent implements PlayerComponent {
    private Player player;

    private Map<Integer, InventoryItem> floorItems;
    private Map<Integer, InventoryItem> wallItems;
    private Map<String, Integer> badges;

    private boolean itemsLoaded = false;

    private Logger log = Logger.getLogger(InventoryComponent.class.getName());

    public InventoryComponent(Player player) {
        this.player = player;

        this.floorItems = new HashMap<>();
        this.wallItems = new HashMap<>();
        this.badges = new HashMap<>();

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
            Map<Integer, InventoryItem> inventoryItems = InventoryDao.getInventoryByPlayerId(this.player.getId());

            for (Map.Entry<Integer, InventoryItem> item : inventoryItems.entrySet()) {
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
            this.badges = InventoryDao.getBadgesByPlayerId(player.getId());
        } catch (Exception e) {
            log.error("Error while loading user badges");
        }
    }

    public void addBadge(String code, boolean insert) {
        if (!badges.containsKey(code)) {
            if (insert) {
                InventoryDao.addBadge(code, this.player.getId());
            }

            // 0 = slot
            this.badges.put(code, 0);

            this.player.getSession().send(new WiredRewardMessageComposer(7));
            this.player.getSession().send(new BadgeInventoryMessageComposer(this.badges));
        }
    }

    public boolean hasBadge(String code) {
        return this.badges.containsKey(code);
    }

    public void removeBadge(String code, boolean delete) {
        if (badges.containsKey(code)) {
            if (delete) {
                InventoryDao.removeBadge(code, player.getId());
            }

            this.badges.remove(code);

            this.player.getSession().send(new AlertMessageComposer(Locale.get("badge.deleted")));
            this.player.getSession().send(new BadgeInventoryMessageComposer(this.badges));
        }
    }

    public void resetBadgeSlots() {
        for (Map.Entry<String, Integer> badge : this.badges.entrySet()) {
            if (badge.getValue() != 0) {
                this.badges.replace(badge.getKey(), 0);
            }
        }
    }

    public Map<String, Integer> equippedBadges() {
        Map<String, Integer> badges = new FastMap<>();

        for (Map.Entry<String, Integer> badge : this.getBadges().entrySet()) {
            if (badge.getValue() > 0)
                badges.put(badge.getKey(), badge.getValue());
        }

        return badges;
    }

    public InventoryItem add(int id, int itemId, String extraData, GiftData giftData) {
        InventoryItem item = new InventoryItem(id, itemId, extraData, giftData);

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
                songItems.add(new SongItem(inventoryItem.getId(), inventoryItem.getDefinition().getSongId()));
            }
        }

        return songItems;
    }

    public void add(int id, int itemId, String extraData) {
        add(id, itemId, extraData, null);
    }

    public void addItem(InventoryItem item) {
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

    public void removeFloorItem(int itemId) {
        this.getFloorItems().remove(itemId);
        this.getPlayer().getSession().send(new RemoveObjectFromInventoryMessageComposer(itemId));
    }

    public void removeWallItem(int itemId) {
        this.getWallItems().remove(itemId);
        this.getPlayer().getSession().send(new RemoveObjectFromInventoryMessageComposer(itemId));
    }

    public boolean hasFloorItem(int id) {
        return this.getFloorItems().containsKey(id);
    }

    public InventoryItem getFloorItem(int id) {
        if (!this.hasFloorItem(id)) {
            return null;
        }
        return this.getFloorItems().get(id);
    }

    public boolean hasWallItem(int id) {
        return this.getWallItems().containsKey(id);
    }

    public InventoryItem getWallItem(int id) {
        if (!this.hasWallItem(id)) {
            return null;
        }
        return this.getWallItems().get(id);
    }

    public InventoryItem getItem(int id) {
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

    public Map<Integer, InventoryItem> getWallItems() {
        return this.wallItems;
    }

    public Map<Integer, InventoryItem> getFloorItems() {
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
