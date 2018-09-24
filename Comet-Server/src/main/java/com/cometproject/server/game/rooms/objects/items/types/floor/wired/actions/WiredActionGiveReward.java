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
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.types.IntervalType;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.types.RewardError;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.types.RewardType;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.wired.WiredRewardMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.utilities.RandomUtil;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.api.data.Data;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import static com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.types.IntervalType.getIntervalByInt;
import static com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.types.RewardType.ALERT;
import static com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.types.RewardType.getCurrencyTypeByKey;


public class WiredActionGiveReward extends WiredActionItem {
    private static final Map<Long, Map<Integer, Long>> rewardTimings = Maps.newConcurrentMap();

    private static final Random RANDOM = new Random();

    private static final int PARAM_HOW_OFTEN = 0;
    private static final int PARAM_UNIQUE = 1;
    private static final int PARAM_TOTAL_REWARD_LIMIT = 2;

    private static final long ONE_DAY = 86400;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_MINUTE = 60;

    private static boolean isAlert = false;

    
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
//        PlayerEntity playerEntity = ((PlayerEntity) event.entity);
//        playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(1));
//        return;


        RewardError error = RewardError.NONE;

        if (this.getWiredData().getParams().size() != 4 || !(event.entity instanceof PlayerEntity) || this.rewards.size() == 0)
            return;
        if (CometSettings.roomWiredRewardMinimumRank > this.ownerRank) return;

        PlayerEntity playerEntity = ((PlayerEntity) event.entity);

        final IntervalType howOften = getIntervalByInt(this.getWiredData().getParams().get(PARAM_HOW_OFTEN));
        final boolean unique = this.getWiredData().getParams().get(PARAM_UNIQUE) == 1;
        final int totalRewardLimit = this.getWiredData().getParams().get(PARAM_TOTAL_REWARD_LIMIT);

        Boolean canReceiveReward = true;

        if (totalRewardLimit > 0)
            if (this.totalRewardCounter >= totalRewardLimit) {
                error = RewardError.OUT_OF_STOCK;
                canReceiveReward = false;
            }

        this.totalRewardCounter++;

        boolean receivedReward = false;

        if(canReceiveReward) {
            for (Reward reward : this.rewards) {
                switch (howOften) {
                    case ONCE:
                        if (this.givenRewards.containsKey(playerEntity.getPlayerId()) && this.givenRewards.get(playerEntity.getPlayerId()).contains(reward.productCode)) {
                            error = RewardError.ALREADY_GIVEN;
                        } else {
                            if (!this.givenRewards.containsKey(playerEntity.getPlayerId())) {
                                this.givenRewards.put(playerEntity.getPlayerId(), new HashSet<>());
                            }

                            this.givenRewards.get(playerEntity.getPlayerId()).add(reward.productCode);
                            StorageContext.getCurrentContext().getRoomItemRepository().saveReward(this.getId(), ((PlayerEntity) event.entity).getPlayerId(), reward.productCode);
                        }

                        if (rewardTimings.get(this.getId()).containsKey(playerEntity.getPlayerId()))
                            error = RewardError.ALREADY_GIVEN;

                        break;

                    case MINUTES:
                        if (rewardTimings.get(this.getId()).containsKey(playerEntity.getPlayerId())) {
                            long lastUserReward = rewardTimings.get(this.getId()).get(playerEntity.getPlayerId());

                            if ((Comet.getTime() - lastUserReward) < ONE_MINUTE) {
                                error = RewardError.ONE_MINUTE;
                            }
                        }
                        break;

                    case DAYS:
                        if (rewardTimings.get(this.getId()).containsKey(playerEntity.getPlayerId())) {
                            long lastUserReward = rewardTimings.get(this.getId()).get(playerEntity.getPlayerId());

                            if ((Comet.getTime() - lastUserReward) < ONE_DAY) {
                                error = RewardError.ONE_DAY;
                            }
                        }
                        break;

                    case HOURS:
                        if (rewardTimings.get(this.getId()).containsKey(playerEntity.getPlayerId())) {
                            long lastReward = rewardTimings.get(this.getId()).get(playerEntity.getPlayerId());

                            if ((Comet.getTime() - lastReward) < ONE_HOUR) {
                                error = RewardError.ONE_HOUR;
                            }
                        }
                        break;

                }

                if (error == RewardError.ONE_DAY || error == RewardError.ONE_HOUR || error == RewardError.UNLUCKY || error == RewardError.ALREADY_GIVEN) {
                    continue;
                }
                boolean probabilityBool;

                if(unique) {
                    probabilityBool = true;
                } else probabilityBool = RandomUtil.getRandomInt(1, 100) <= reward.probability;

                System.out.println("a");

                if (probabilityBool) {
                    if (reward.isBadge) {
                        if (!playerEntity.getPlayer().getInventory().hasBadge(reward.productCode)) {
                            playerEntity.getPlayer().getInventory().addBadge(reward.productCode, true);
                        }
                    } else {
                        String[] itemData = reward.productCode.contains("%") ? reward.productCode.split("%") : reward.productCode.split(":");

                        if (isCurrencyReward(itemData[0])) {
                            RewardType amount = getCurrencyTypeByKey(itemData[0]);

                            int value = 0;

                            try {
                                if(amount != ALERT) {
                                    value = Integer.parseInt(itemData[1]);
                                }
                            } catch (Exception ignored) {
                                return;
                            }


                            switch (amount) {
                                case CREDITS:
                                    playerEntity.getPlayer().getData().increaseCredits(value);
                                    playerEntity.getPlayer().getSession().send(new AlertMessageComposer(
                                            Locale.getOrDefault("wired.reward.coins", "You received %s coin(s)!").replace("%s", value + "")));
                                    break;

                                case ACTIVITY_POINTS:
                                    playerEntity.getPlayer().getData().increaseActivityPoints(value);
                                    playerEntity.getPlayer().getSession().send(new AlertMessageComposer(
                                            Locale.getOrDefault("wired.reward.duckets", "You received %s ducket(s)!").replace("%s", value + "")));
                                    break;

                                case VIP_POINTS:
                                    playerEntity.getPlayer().getData().increaseVipPoints(value);
                                    playerEntity.getPlayer().getSession().send(new AlertMessageComposer(
                                            Locale.getOrDefault("wired.reward.diamonds", "You received %s diamond(s)!").replace("%s", value + "")));
                                    break;

                                case SEASONAL_POINTS:
                                    playerEntity.getPlayer().getData().increaseSeasonalPoints(value);
                                    playerEntity.getPlayer().getSession().send(new AlertMessageComposer(
                                            Locale.getOrDefault("wired.reward.seasonal", "You received %s seasonal(s)!").replace("%s", value + "")));
                                    break;

                                case GO_TO_ROOM:
                                    playerEntity.getPlayer().getSession().send(new RoomForwardMessageComposer(value));
                                    break;

                                case ALERT:
                                    isAlert = true;
                                    final StringBuilder mergedParams = new StringBuilder();

                                    for (int i = 0; i < itemData.length; i++) {
                                        if (i >= 1) {
                                            mergedParams.append(itemData[i]).append(" ");
                                        }
                                    }

                                    String message = mergedParams.toString();


                                    playerEntity.getPlayer().getSession().send(new AlertMessageComposer(message));
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

                } else
                    error = RewardError.UNLUCKY;


            }
        }

        receivedReward = true;



        if (error == RewardError.ONE_DAY || error == RewardError.ONE_HOUR || error == RewardError.UNLUCKY || error == RewardError.ALREADY_GIVEN || error == RewardError.OUT_OF_STOCK) {
            playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(error.getInteger()));
            return;
        }

        if (!receivedReward) {
            playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(4));
        } else {
            if(!isAlert) {
                playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(6));
            }
        }

        if (rewardTimings.get(this.getId()).containsKey(playerEntity.getPlayerId())) {
            rewardTimings.get(this.getId()).replace(playerEntity.getPlayerId(), Comet.getTime());
        } else {
            rewardTimings.get(this.getId()).put(playerEntity.getPlayerId(), Comet.getTime());
        }

    }

    private boolean isCurrencyReward(final String key) {
        return getCurrencyTypeByKey(key) != null;
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

    private class Reward {
        private boolean isBadge;
        private String productCode;
        private int probability;

        private Reward(boolean isBadge, String productCode, int probability) {
            this.isBadge = isBadge;
            this.productCode = productCode;
            this.probability = probability;
        }
    }
}
