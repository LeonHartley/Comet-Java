package com.cometproject.storage.mysql.queues.pets;

import com.cometproject.api.game.pets.IPetStats;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.MySQLStorageQueue;

import java.sql.PreparedStatement;
import java.util.concurrent.ScheduledExecutorService;

public class PetStatsUpdateQueue extends MySQLStorageQueue<Integer, IPetStats> {
    public PetStatsUpdateQueue(long delayMilliseconds, ScheduledExecutorService executorService, MySQLConnectionProvider connectionProvider) {
        super("UPDATE pet_data SET scratches = ?, level = ?, happiness = ?, experience = ?, energy = ?, hunger = ? WHERE id = ?;", delayMilliseconds, executorService, connectionProvider);
    }

    @Override
    protected void processBatch(PreparedStatement preparedStatement, Integer id, IPetStats pet) throws Exception {
        preparedStatement.setInt(1, pet.getScratches());
        preparedStatement.setInt(2, pet.getLevel());
        preparedStatement.setInt(3, pet.getHappiness());
        preparedStatement.setInt(4, pet.getExperience());
        preparedStatement.setInt(5, pet.getEnergy());
        preparedStatement.setInt(6, pet.getHunger());

        preparedStatement.setInt(7, pet.getId());

        preparedStatement.addBatch();
    }
}
