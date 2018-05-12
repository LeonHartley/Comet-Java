package com.cometproject.storage.mysql.repositories;

import com.cometproject.storage.api.repositories.IRewardRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;

import java.util.function.Consumer;

public class MySQLRewardRepository extends MySQLRepository implements IRewardRepository {
    public MySQLRewardRepository(MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public void playerReceivedReward(int playerId, String badgeCode, Consumer<Boolean> consumer) {
        select("SELECT COUNT(0) FROM player_badges WHERE player_id = ? AND badge_code = ?",  (data) -> {
            final int count = data.readInteger(0);

            consumer.accept(count == 1);
        }, playerId);
    }

    @Override
    public void giveReward(int playerId, String badgeCode, int points) {
        transaction(transaction -> {
            update("INSERT into player_events (player_id, events) VALUES(?, 1) ON DUPLICATE KEY UPDATE events = events + 1;", transaction, playerId);
            update("INSERT into player_badges (player_id, badge_code) VALUES(?, ?);", transaction, playerId, badgeCode);
            update("UPDATE players SET vip_points = vip_points + ? WHERE id = ?", true, playerId, points);

            transaction.commit();
        });
    }
}
