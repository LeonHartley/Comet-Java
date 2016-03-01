package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.wired.WiredRewardMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;

import java.util.*;


public class WiredActionGiveReward extends WiredActionItem {
    private static final Map<Long, Map<Integer, Long>> rewardTimings = Maps.newConcurrentMap();
    private static final Random RANDOM = new Random();

    private static final int PARAM_HOW_OFTEN = 0;

    private static final int PARAM_UNIQUE = 1;
    private static final int PARAM_TOTAL_REWARD_LIMIT = 2;
    private static final int REWARD_LIMIT_ONCE = 0;

    private static final int REWARD_LIMIT_DAY = 1;
    private static final int REWARD_LIMIT_HOUR = 2;

    public static final String REWARD_DIAMONDS = "diamonds";
    public static final String REWARD_COINS = "coins";
    public static final String REWARD_DUCKETS = "duckets";

    private static final long ONE_DAY = 86400;
    private static final long ONE_HOUR = 3600;

    // increments and will be reset when the room is unloaded.
    private int totalRewardCounter = 0;

    private List<Reward> rewards;
    private Set<Integer> givenRewards;

    private final int ownerRank;

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionGiveReward(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if (!rewardTimings.containsKey(this.getId())) {
            rewardTimings.put(this.getId(), Maps.newConcurrentMap());
        }

        final PlayerData playerData = PlayerManager.getInstance().getDataByPlayerId(this.ownerId);

        if (playerData != null) {
            this.ownerRank = playerData.getRank();
        } else {
            this.ownerRank = 1;
        }

        this.givenRewards = RoomItemDao.getGivenRewards(this.getId());
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
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.getWiredData().getParams().size() != 4 || !(entity instanceof PlayerEntity) || this.rewards.size() == 0) {
            return false;
        }

        if (CometSettings.roomWiredRewardMinimumRank > this.ownerRank) return false;

        PlayerEntity playerEntity = ((PlayerEntity) entity);

        final int howOften = this.getWiredData().getParams().get(PARAM_HOW_OFTEN);
        final boolean unique = this.getWiredData().getParams().get(PARAM_UNIQUE) == 1;
        final int totalRewardLimit = this.getWiredData().getParams().get(PARAM_TOTAL_REWARD_LIMIT);

        int errorCode = -1;

        switch (howOften) {
            case REWARD_LIMIT_ONCE:
                if(this.givenRewards.contains(playerEntity.getPlayerId())) {
                    errorCode = 1;
                } else {
                    this.givenRewards.add(playerEntity.getPlayerId());
                    RoomItemDao.saveReward(this.getId(), ((PlayerEntity) entity).getPlayerId());
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
            playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(errorCode));
            return false;
        }

        this.totalRewardCounter++;

        boolean receivedReward = false;

        for (Reward reward : this.rewards) {
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
                                playerEntity.getPlayer().getData().increasePoints(amount);
                                playerEntity.getPlayer().getSession().send(new AlertMessageComposer(
                                        Locale.getOrDefault("wired.reward.diamonds", "You received %s diamond(s)!").replace("%s", amount + "")));
                                break;

                            case REWARD_DUCKETS:
                                playerEntity.getPlayer().getData().increaseActivityPoints(amount);
                                playerEntity.getPlayer().getSession().send(new AlertMessageComposer(
                                        Locale.getOrDefault("wired.reward.duckets", "You received %s ducket(s)!").replace("%s", amount + "")));
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

                        ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(itemId);

                        if (itemDefinition != null) {
                            long newItem = ItemDao.createItem(playerEntity.getPlayerId(), itemId, extraData);

                            PlayerItem playerItem = new InventoryItem(newItem, itemId, extraData);

                            playerEntity.getPlayer().getInventory().addItem(playerItem);

                            playerEntity.getPlayer().getSession().send(new UpdateInventoryMessageComposer());
                            playerEntity.getPlayer().getSession().send(new UnseenItemsMessageComposer(Sets.newHashSet(playerItem)));

                            playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(6));
                        }
                    }
                }

                receivedReward = true;
            }
        }


        if (!receivedReward) {
            playerEntity.getPlayer().getSession().send(new WiredRewardMessageComposer(4));
        }

        if (rewardTimings.get(this.getId()).containsKey(playerEntity.getPlayerId())) {
            rewardTimings.get(this.getId()).replace(playerEntity.getPlayerId(), Comet.getTime());
        } else {
            rewardTimings.get(this.getId()).put(playerEntity.getPlayerId(), Comet.getTime());
        }
        return false;
    }

    private boolean isCurrencyReward(final String key) {
        return (key.equals(REWARD_COINS) || key.equals(REWARD_DIAMONDS) || key.equals(REWARD_DUCKETS));
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
