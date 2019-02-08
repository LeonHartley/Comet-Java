package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.furniture.types.IGiftData;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.furniture.types.SongItem;
import com.cometproject.api.game.players.data.IPlayerComponent;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PlayerInventory extends IPlayerComponent {
    void loadItems(int id);

    void loadBadges();

    void loadEffects();

    void send();

    void addBadge(String code, boolean insert);

    void addBadge(String code, boolean insert, boolean sendAlert);

    void addBadge(String code, boolean insert, boolean sendAlert, boolean isAchievement);

    boolean hasBadge(String code);

    void removeBadge(String code, boolean delete);

    void removeBadge(String code, boolean delete, boolean sendAlert, boolean sendUpdate);

    void achievementBadge(String achievement, int level);

    void resetBadgeSlots();

    String[] equippedBadges();

    PlayerItem add(long id, int itemId, String extraData, IGiftData giftData, LimitedEditionItem limitedEditionItem);

    List<SongItem> getSongs();

    void add(long id, int itemId, String extraData, LimitedEditionItem limitedEditionItem);

    void addItem(PlayerItem item);

    void removeItem(PlayerItem item);

    void removeItem(long itemId);

    boolean hasItem(long id);

    PlayerItem getItem(long id);

    int getTotalSize();

    Map<Long, PlayerItem> getInventoryItems();

    Map<String, Integer> getBadges();

    boolean hasEffect(int effectId);

    Set<Integer> getEffects();

    int getEquippedEffect();

    void setEquippedEffect(int effectId);

    boolean itemsLoaded();

    boolean isViewingInventory();

    int viewingInventoryUserId();
}
