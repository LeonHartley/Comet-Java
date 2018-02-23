package com.cometproject.storage.mysql.queues.items;

import com.cometproject.api.game.rooms.objects.IFloorItem;
import com.cometproject.storage.mysql.BlockingMySQLStorageQueue;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.MySQLStorageQueue;

import java.sql.PreparedStatement;
import java.util.concurrent.ScheduledExecutorService;

public class ItemUpdateQueue extends BlockingMySQLStorageQueue<Long, IFloorItem> {

    public ItemUpdateQueue(MySQLConnectionProvider connectionProvider) {
        super(connectionProvider, "UPDATE items SET x = ?, y = ?, z = ?, rot = ?, extra_data = ? WHERE id = ?;", 25);
    }

    @Override
    public void processBatch(PreparedStatement preparedStatement, Long id, IFloorItem floor) throws Exception {
        preparedStatement.setInt(1, floor.getPosition().getX());
        preparedStatement.setInt(2, floor.getPosition().getY());
        preparedStatement.setDouble(3, floor.getPosition().getZ());
        preparedStatement.setInt(4, floor.getRotation());
        preparedStatement.setString(5, floor.getDataObject());
        preparedStatement.setLong(6, floor.getId());

        preparedStatement.addBatch();
    }
}
