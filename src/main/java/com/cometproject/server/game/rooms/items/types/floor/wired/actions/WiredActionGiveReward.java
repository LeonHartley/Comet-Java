package com.cometproject.server.game.rooms.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredActionItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class WiredActionGiveReward extends WiredActionItem {
    private static int PARAM_HOW_OFTEN = 0;
    private static int PARAM_UNIQUE = 1;
    private static int PARAM_TOTAL_REWARD_LIMIT = 2;

    private static int REWARD_LIMIT_ONCE = 0;
    private static int REWARD_LIMIT_DAY = 1;
    private static int REWARD_LIMIT_HOUR = 2;

    // increments and will be reset when the room is unloaded.
    private int totalRewardsLimit = 0;

    private List<Reward> rewards = Lists.newArrayList();
    private Map<Integer, Long> rewardsWithLastReceive = Maps.newHashMap();

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param roomId   The ID of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionGiveReward(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
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
    public boolean evaluate(GenericEntity entity, Object data) {
        if(this.getWiredData().getParams().size() != 3) {
            return false; // no params! what do??
        }

        final int howOften = this.getWiredData().getParams().get(PARAM_HOW_OFTEN);
        final boolean unique = this.getWiredData().getParams().get(PARAM_UNIQUE) == 1;
        final int totalRewardLimit = this.getWiredData().getParams().get(PARAM_TOTAL_REWARD_LIMIT);


        return false;
    }

    @Override
    public void onDataRefresh() {
        this.rewards.clear();

        final String[] data = this.getWiredData().getText().split(";");

        for(String reward : data) {
            final String[] rewardData = reward.split(",");
            if(reward.length() != 3 || !StringUtils.isNumeric(rewardData[2])) continue;

            this.rewards.add(new Reward(rewardData[0].equals("0"), rewardData[1], Integer.parseInt(rewardData[2])));
        }
    }

    public class Reward {
        private boolean isBadge;
        private String productCode;
        private int probability;

        public Reward(boolean isBadge,  String productCode, int probability) {
            this.isBadge = isBadge;
            this.productCode = productCode;
            this.probability = probability;
        }

        public boolean isBadge() {
            return isBadge;
        }

        public String getProductCode() {
            return productCode;
        }

        public int getProbability() {
            return probability;
        }
    }
}
