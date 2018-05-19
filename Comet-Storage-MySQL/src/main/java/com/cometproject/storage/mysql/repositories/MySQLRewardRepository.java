package com.cometproject.storage.mysql.repositories;

import com.cometproject.storage.api.data.rewards.RewardData;
import com.cometproject.storage.api.repositories.IRewardRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MySQLRewardRepository extends MySQLRepository implements IRewardRepository {
    public MySQLRewardRepository(MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public void playerReceivedReward(int playerId, String badgeCode, Consumer<Boolean> consumer) {
        select("SELECT COUNT(0) FROM player_badges WHERE player_id = ? AND badge_code = ?", (data) -> {
            final int count = data.readInteger(1);

            consumer.accept(count == 1);
        }, playerId);
    }

    @Override
    public void giveReward(int playerId, String badgeCode, int points) {
        transaction(transaction -> {
            update("INSERT into player_events (player_id, events) VALUES(?, 1) ON DUPLICATE KEY UPDATE events = events + 1;", transaction, playerId);
            update("INSERT into player_badges (player_id, badge_code) VALUES(?, ?);", transaction, playerId, badgeCode);
            update("UPDATE players SET vip_points = vip_points + ? WHERE id = ?", transaction, points, playerId);

            transaction.commit();
        });
    }

    @Override
    public void getActiveRewards(Consumer<Map<String, RewardData>> consumer) {
        final Map<String, RewardData> rewards = Maps.newConcurrentMap();

        select("SELECT code, badge, diamonds FROM player_rewards WHERE active = ?", (data) -> {
            rewards.put(data.readString("code"), new RewardData(data.readString("code"), data.readString("badge"), data.readInteger("diamonds")));
        }, "1");

        consumer.accept(rewards);
    }

    @Override
    public void playerRedeemedReward(int playerId, String code, Consumer<Boolean> consumer) {
        select("SELECT COUNT(0) FROM player_rewards_redeemed WHERE player_id = ? AND reward_code = ?;", (data) -> {
            final int count = data.readInteger(1);

            consumer.accept(count == 1);
        }, playerId, code);
    }

    @Override
    public void redeemReward(int playerId, String rewardCode, String badgeCode, int diamonds) {
        transaction(transaction -> {
            update("INSERT into player_rewards_redeemed (player_id, reward_code) VALUES(?, ?);", transaction, playerId, rewardCode);
            update("INSERT into player_badges (player_id, badge_code) VALUES(?, ?);", transaction, playerId, badgeCode);
            update("UPDATE players SET vip_points = vip_points + ? WHERE id = ?", transaction, diamonds, playerId);

            transaction.commit();
        });
    }
}
