package com.cometproject.server.storage.queue.types;

import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queue.StorageQueue;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PlayerDataStorageQueue implements Initializable, CometTask, StorageQueue<PlayerData> {
    private static final Logger log = Logger.getLogger(PlayerDataStorageQueue.class.getName());
    private static PlayerDataStorageQueue instance;

    private ScheduledFuture future;

    private Map<Integer, PlayerData> playerData;

    public PlayerDataStorageQueue() {
        this.playerData = new ConcurrentHashMap<>();
    }

    @Override
    public void initialize() {
        this.future = CometThreadManager.getInstance().executePeriodic(this, 0, 1500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if(playerData.size() == 0) {
            // No data to be saved!
            return;
        }

        log.debug("Saving " + this.playerData.size() + " player data instances");

        PlayerDao.saveBatch(this.playerData);

        this.playerData.clear();
    }

    @Override
    public void queueSave(final PlayerData playerData) {
        if(this.playerData.get(playerData.getId()) != null) {
            this.playerData.replace(playerData.getId(), playerData);
            return;
        }

        this.playerData.put(playerData.getId(), playerData);
    }

    @Override
    public void unqueue(PlayerData playerData) {
        if(this.playerData.containsKey(playerData.getId())) {
            this.playerData.remove(playerData.getId());
        }
    }

    @Override
    public boolean isQueued(PlayerData object) {
        return this.playerData.containsKey(object.getId());
    }

    public boolean isPlayerSaving(int playerId) {
        return this.playerData.containsKey(playerId);
    }

    public PlayerData getPlayerData(int playerId) {
        return this.playerData.get(playerId);
    }

    @Override
    public void shutdown() {
        this.future.cancel(false);

        this.run();
    }

    public static PlayerDataStorageQueue getInstance() {
        if (instance == null) {
            instance = new PlayerDataStorageQueue();
        }

        return instance;
    }
}
