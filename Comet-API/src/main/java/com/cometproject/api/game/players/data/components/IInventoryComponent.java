package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.furniture.types.ILimitedEditionItem;
import com.cometproject.api.game.furniture.types.ISongItem;
import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.furniture.types.IGiftData;
import com.cometproject.api.game.players.data.components.inventory.IInventoryItem;

import java.util.List;
import java.util.Map;

public interface IInventoryComponent {
    void loadItems();

    void loadBadges();

    void addBadge(String code, boolean insert);

    void addBadge(String code, boolean insert, boolean sendAlert);

    boolean hasBadge(String code);

    void removeBadge(String code, boolean delete);

    void removebadge(String code, boolean delete, boolean sendAlert);

    void removeBadge(String code, boolean delete, boolean sendAlert, boolean sendUpdate);

    void achievementBadge(String achievement, int level);

    void resetBadgeSlots();

    Map<String, Integer> equippedBadges();

    IInventoryItem add(long id, int itemId, String extraData, IGiftData giftData, ILimitedEditionItem limitedEditionItem);

    List<ISongItem> getSongs();

    void add(long id, int itemId, String extraData, ILimitedEditionItem limitedEditionItem);

    void addItem(IInventoryItem item);

    void removeItem(IInventoryItem item);

    void removeFloorItem(long itemId);

    void removeWallItem(long itemId);

    boolean hasFloorItem(long id);

    IInventoryItem getFloorItem(long id);

    boolean hasWallItem(long id);

    @Deprecated
    IInventoryItem getWallItem(int id);

    @Deprecated
    IInventoryItem getFloorItem(int id);

    IInventoryItem getWallItem(long id);

    IInventoryItem getItem(long id);

    void dispose();

    int getTotalSize();

    Map<Long, IInventoryItem> getWallItems();

    Map<Long, IInventoryItem> getFloorItems();

    Map<String, Integer> getBadges();

    IPlayer getPlayer();

    boolean itemsLoaded();
}
