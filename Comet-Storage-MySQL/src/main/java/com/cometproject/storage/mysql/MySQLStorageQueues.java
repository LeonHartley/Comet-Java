package com.cometproject.storage.mysql;

import com.cometproject.storage.mysql.queues.items.ItemDataUpdateQueue;
import com.cometproject.storage.mysql.queues.items.ItemUpdateQueue;
import com.cometproject.storage.mysql.queues.pets.PetStatsUpdateQueue;
import com.cometproject.storage.mysql.queues.players.PlayerBadgeUpdateQueue;
import com.cometproject.storage.mysql.queues.players.PlayerDataUpdateQueue;
import com.cometproject.storage.mysql.queues.players.PlayerOfflineUpdateQueue;
import com.cometproject.storage.mysql.queues.players.PlayerStatusUpdateQueue;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MySQLStorageQueues {
    private static MySQLStorageQueues instance;

    // Do we need to make this configurable? Or should we integrate this in with the other thread pools..?
    private final ScheduledExecutorService storageExecutor = Executors.newScheduledThreadPool(4);

    private final PlayerStatusUpdateQueue playerStatusQueue;
    private final PlayerOfflineUpdateQueue playerOfflineUpdateQueue;
    private final ItemDataUpdateQueue itemDataUpdateQueue;
    private final ItemUpdateQueue itemUpdateQueue;
    private final PetStatsUpdateQueue petStatsUpdateQueue;
    private final PlayerDataUpdateQueue playerDataUpdateQueue;
    private final PlayerBadgeUpdateQueue playerBadgeUpdateQueue;

    public MySQLStorageQueues(MySQLConnectionProvider connectionProvider) {
        this.playerStatusQueue = new PlayerStatusUpdateQueue(2500, this.storageExecutor, connectionProvider);
        this.playerOfflineUpdateQueue = new PlayerOfflineUpdateQueue(1000, this.storageExecutor, connectionProvider);
        this.itemDataUpdateQueue = new ItemDataUpdateQueue(2500, this.storageExecutor, connectionProvider);
        this.itemUpdateQueue = new ItemUpdateQueue(2500, this.storageExecutor, connectionProvider);
        this.petStatsUpdateQueue = new PetStatsUpdateQueue(1000, this.storageExecutor, connectionProvider);
        this.playerDataUpdateQueue = new PlayerDataUpdateQueue(1000, this.storageExecutor, connectionProvider);
        this.playerBadgeUpdateQueue = new PlayerBadgeUpdateQueue(5000, this.storageExecutor, connectionProvider);
    }

    public static MySQLStorageQueues instance() {
        return instance;
    }

    public PlayerStatusUpdateQueue getPlayerStatusQueue() {
        return playerStatusQueue;
    }

    public PlayerOfflineUpdateQueue getPlayerOfflineUpdateQueue() {
        return playerOfflineUpdateQueue;
    }

    public ItemDataUpdateQueue getItemDataUpdateQueue() {
        return itemDataUpdateQueue;
    }

    public ItemUpdateQueue getItemUpdateQueue() {
        return itemUpdateQueue;
    }

    public PetStatsUpdateQueue getPetStatsUpdateQueue() {
        return petStatsUpdateQueue;
    }

    public PlayerDataUpdateQueue getPlayerDataUpdateQueue() {
        return playerDataUpdateQueue;
    }

    public PlayerBadgeUpdateQueue getPlayerBadgeUpdateQueue() {
        return playerBadgeUpdateQueue;
    }

    public static void setInstance(MySQLStorageQueues storageContext) {
        instance = storageContext;
    }

    public void shutdown() {
        this.playerStatusQueue.stop();
        this.playerOfflineUpdateQueue.stop();
        this.itemDataUpdateQueue.stop();
        this.itemUpdateQueue.stop();
        this.petStatsUpdateQueue.stop();
        this.playerDataUpdateQueue.stop();
        this.playerBadgeUpdateQueue.stop();
    }
}
