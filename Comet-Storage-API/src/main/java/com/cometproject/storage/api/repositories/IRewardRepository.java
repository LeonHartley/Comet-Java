package com.cometproject.storage.api.repositories;

import com.cometproject.storage.api.data.rewards.RewardData;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface IRewardRepository {
    void playerReceivedReward(int playerId, String badgeCode, Consumer<Boolean> consumer);

    void giveReward(int playerId, String badgeCode, int points, int seasonal);

    void getActiveRewards(Consumer<Map<String, RewardData>> consumer);

    void playerRedeemedReward(int playerId, String code, Consumer<Boolean> consumer);

    void redeemReward(int playerId, RewardData rewardData);
}
