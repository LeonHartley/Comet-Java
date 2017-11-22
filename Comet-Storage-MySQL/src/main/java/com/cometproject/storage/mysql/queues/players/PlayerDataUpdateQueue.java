package com.cometproject.storage.mysql.queues.players;

import com.cometproject.api.game.players.data.IPlayerData;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.MySQLStorageQueue;

import java.sql.PreparedStatement;
import java.util.concurrent.ScheduledExecutorService;

public class PlayerDataUpdateQueue extends MySQLStorageQueue<Integer, IPlayerData> {
    public PlayerDataUpdateQueue(long delayMilliseconds, ScheduledExecutorService executorService, MySQLConnectionProvider connectionProvider) {
        super("UPDATE players SET username = ?, motto = ?, figure = ?, credits = ?, vip_points = ?, gender = ?, favourite_group = ?, activity_points = ?, quest_id = ?, achievement_points = ? WHERE id = ?;", delayMilliseconds, executorService, connectionProvider);
    }

    @Override
    protected void processBatch(PreparedStatement preparedStatement, Integer id, IPlayerData player) throws Exception {
        preparedStatement.setString(1, player.getUsername());
        preparedStatement.setString(2, player.getMotto());
        preparedStatement.setString(3, player.getFigure());
        preparedStatement.setInt(4, player.getCredits());
        preparedStatement.setInt(5, player.getVipPoints());
        preparedStatement.setString(6, player.getGender());
        preparedStatement.setInt(7, player.getFavouriteGroup());
        preparedStatement.setInt(8, player.getActivityPoints());
        preparedStatement.setInt(9, player.getQuestId());
        preparedStatement.setInt(10, player.getAchievementPoints());
        preparedStatement.setInt(11, player.getId());

        preparedStatement.addBatch();
    }
}
