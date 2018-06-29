package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.composers.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.wired.WiredRewardMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.api.data.Data;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hdfs.server.common.Storage;

import java.util.*;


public class WiredActionGiveReward extends WiredActionItem {
    private static final String REWARD_DIAMONDS = "diamonds";
    private static final String REWARD_COINS = "coins";
    private static final String REWARD_DUCKETS = "duckets";
    private static final String REWARD_SEASONAL = "seasonal";

    private static final String REWARD_GOROOM = "goto";

    private static final Map<Long, Map<Integer, Long>> rewardTimings = Maps.newConcurrentMap();

    private static final Random RANDOM = new Random();

    private static final int PARAM_HOW_OFTEN = 0;
    private static final int PARAM_UNIQUE = 1;
    private static final int PARAM_TOTAL_REWARD_LIMIT = 2;

    private static final int REWARD_LIMIT_ONCE = 0;
    private static final int REWARD_LIMIT_DAY = 1;
    private static final int REWARD_LIMIT_HOUR = 2;

    private static final long ONE_DAY = 86400;
    private static final long ONE_HOUR = 3600;

    private final int ownerRank;
    // increments and will be reset when the room is unloaded.

    private int totalRewardCounter = 0;
    private List<Reward> rewards;
    private Map<Integer, Set<String>> givenRewards;

    public WiredActionGiveReward(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        if (!rewardTimings.containsKey(this.getId())) {
            rewardTimings.put(this.getId(), Maps.newConcurrentMap());
        }

        final PlayerData playerData = PlayerManager.getInstance().getDataByPlayerId(this.getItemData().getOwnerId());

        if (playerData != null) {
            this.ownerRank = playerData.getRank();
        } else {
            this.ownerRank = 1;
        }

        final Data<Map<Integer, Set<String>>> rewardData = Data.createEmpty();

        StorageContext.getCurrentContext().getRoomItemRepository().getGivenRewards(this.getId(), rewardData::set);

        if (rewardData.has()) {
            this.givenRewards = rewardData.get();
        } else {
            this.givenRewards = Maps.newConcurrentMap();
        }
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 17;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (this.getWiredData().getParams().size() != 4 || !(event.entity instanceof PlayerEntity) || this.rewards.size() == 0) {
            return;
        }

        if (CometSettings.roomWiredRewardMinimumRank > this.ownerRank) return;

        PlayerEntity playerEntity = ((PlayerEntity) event.entity);

        final int howOften = this.getWiredData().getParams().get(PARAM_HOW_OFTEN);
        final boolean unique = this.getWiredData().getParams().get(PARAM_UNIQUE) == 1;
        final int totalRewardLimit = this.getWiredData().getParams().get(PARAM_TOTAL_REWARD_LIMIT);

        int errorCode = -1;

        if (totalRewardLimit != 0) {
            if (this.totalRewardCounter >= totalRewardLimit) {
                errorCode = 0;
            }
        }

        if (errorCode != -1) {
            playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(errorCode));
            return;
        }

        this.totalRewardCounter++;

        boolean receivedReward = false;

        errorCode = -1;

        for (Reward reward : this.rewards) {
            switch (howOften) {
                case REWARD_LIMIT_ONCE:
                    if (this.givenRewards.containsKey(playerEntity.getPlayerId()) && this.givenRewards.get(playerEntity.getPlayerId()).contains(reward.productCode)) {
                        errorCode = 1;
                    } else {
                        if (!this.givenRewards.containsKey(playerEntity.getPlayerId())) {
                            this.givenRewards.put(playerEntity.getPlayerId(), new HashSet<>());
                        }

                        this.givenRewards.get(playerEntity.getPlayerId()).add(reward.productCode);
                        StorageContext.getCurrentContext().getRoomItemRepository().saveReward(this.getId(), ((PlayerEntity) event.entity).getPlayerId(), reward.productCode);
                    }

                    if (rewardTimings.get(this.getId()).containsKey(playerEntity.getPlayerId())) {
                        errorCode = 1;
                    }

                    break;

                case REWARD_LIMIT_DAY:
                    if (rewardTimings.get(this.getId()).containsKey(playerEntity.getPlayerId())) {
                        long lastReward = rewardTimings.get(this.getId()).get(playerEntity.getPlayerId());

                        if ((Comet.getTime() - lastReward) < ONE_DAY) {
                            errorCode = 2;
                        }
                    }
                    break;

                case REWARD_LIMIT_HOUR:
                    if (rewardTimings.get(this.getId()).containsKey(playerEntity.getPlayerId())) {
                        long lastReward = rewardTimings.get(this.getId()).get(playerEntity.getPlayerId());

                        if ((Comet.getTime() - lastReward) < ONE_HOUR) {
                            errorCode = 3;
                        }
                    }
                    break;
            }

            if (totalRewardLimit != 0) {
                if (this.totalRewardCounter >= totalRewardLimit) {
                    errorCode = 0;
                }
            }

            if (errorCode != -1) {
                continue;
            }

            boolean giveReward = unique || ((reward.probability / 100) <= RANDOM.nextDouble());

            if (giveReward && !receivedReward) {
                if (reward.isBadge) {
                    if (!playerEntity.getPlayer().getInventory().hasBadge(reward.productCode)) {
                        playerEntity.getPlayer().getInventory().addBadge(reward.productCode, true);
                    }
                } else {
                    String[] itemData = reward.productCode.contains("%") ? reward.productCode.split("%") : reward.productCode.split(":");

                    if (isCurrencyReward(itemData[0])) {
                        // handle currency reward
                        if (itemData.length != 2) continue;

                        if (!StringUtils.isNumeric(itemData[1])) {
                            continue;
                        }

                        int amount = Integer.parseInt(itemData[1]);

                        switch (itemData[0]) {
                            case REWARD_COINS:
                                playerEntity.getPlayer().getData().increaseCredits(amount);
                                playerEntity.getPlayer().getSession().send(new AlertMessageComposer(
                                        Locale.getOrDefault("wired.reward.coins", "You received %s coin(s)!").replace("%s", amount + "")));
                                break;

                            case REWARD_DIAMONDS:
                                playerEntity.getPlayer().getData().increaseVipPoints(amount);
                                playerEntity.getPlayer().getSession().send(new AlertMessageComposer(
                                        Locale.getOrDefault("wired.reward.diamonds", "You received %s diamond(s)!").replace("%s", amount + "")));
                                break;

                            case REWARD_DUCKETS:
                                playerEntity.getPlayer().getData().increaseActivityPoints(amount);
                                playerEntity.getPlayer().getSession().send(new AlertMessageComposer(
                                        Locale.getOrDefault("wired.reward.duckets", "You received %s ducket(s)!").replace("%s", amount + "")));
                                break;

                            case REWARD_SEASONAL:
                                playerEntity.getPlayer().getData().increaseSeasonalPoints(amount);
                                playerEntity.getPlayer().getSession().send(new AlertMessageComposer(
                                        Locale.getOrDefault("wired.reward.seasonal", "You received %s seasonal(s)!").replace("%s", amount + "")));
                                break;

                            case REWARD_GOROOM:
                                playerEntity.getPlayer().getSession().send(new RoomForwardMessageComposer(amount));
                                break;
                        }

                        playerEntity.getPlayer().getData().save();
                        playerEntity.getPlayer().sendBalance();
                    } else {

                        String extraData = "0";

                        if (itemData.length == 2) {
                            extraData = itemData[1];
                        }

                        if (!StringUtils.isNumeric(itemData[0]))
                            continue;

                        int itemId = Integer.parseInt(itemData[0]);

                        FurnitureDefinition itemDefinition = ItemManager.getInstance().getDefinition(itemId);

                        if (itemDefinition != null) {
                            final Data<Long> newItem = Data.createEmpty();
                            StorageContext.getCurrentContext().getRoomItemRepository().createItem(playerEntity.getPlayerId(), itemId, extraData, newItem::set);

                            PlayerItem playerItem = new InventoryItem(newItem.get(), itemId, extraData);

                            playerEntity.getPlayer().getInventory().addItem(playerItem);

                            playerEntity.getPlayer().getSession().send(new UpdateInventoryMessageComposer());
                            playerEntity.getPlayer().getSession().send(new UnseenItemsMessageComposer(Sets.newHashSet(playerItem), ItemManager.getInstance()));
                        }
                    }
                }

                receivedReward = true;
            }
        }

        if (errorCode != -1) {
            playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(errorCode));
            return;
        }

        if (!receivedReward) {
            playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(4));
        } else {
            playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(6));
        }

        if (rewardTimings.get(this.getId()).containsKey(playerEntity.getPlayerId())) {
            rewardTimings.get(this.getId()).replace(playerEntity.getPlayerId(), Comet.getTime());
        } else {
            rewardTimings.get(this.getId()).put(playerEntity.getPlayerId(), Comet.getTime());
        }
    }

    private boolean isCurrencyReward(final String key) {
        return (key.equals(REWARD_COINS) || key.equals(REWARD_DIAMONDS) || key.equals(REWARD_DUCKETS)
                || key.equals(REWARD_SEASONAL) || key.equals(REWARD_GOROOM));
    }

    @Override
    public void onDataRefresh() {
        if (this.rewards == null)
            this.rewards = Lists.newArrayList();
        else
            this.rewards.clear();

        final String[] data = this.getWiredData().getText().split(";");

        for (String reward : data) {
            final String[] rewardData = reward.split(",");
            if (rewardData.length != 3 || !StringUtils.isNumeric(rewardData[2])) continue;

            this.rewards.add(new Reward(rewardData[0].equals("0"), rewardData[1], Integer.parseInt(rewardData[2])));
        }
    }

    @Override
    public void onUnload() {
        this.givenRewards.clear();
    }

    @Override
    public void onPickup() {
        super.onPickup();
        rewardTimings.get(this.getId()).clear();
        rewardTimings.remove(this.getId());
    }

    public class Reward {
        private boolean isBadge;
        private String productCode;
        private int probability;

        public Reward(boolean isBadge, String productCode, int probability) {
            this.isBadge = isBadge;
            this.productCode = productCode;
            this.probability = probability;
        }
    }
}
