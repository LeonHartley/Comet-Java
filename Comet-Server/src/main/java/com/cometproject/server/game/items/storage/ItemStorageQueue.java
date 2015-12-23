package com.cometproject.server.game.items.storage;

import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ItemStorageQueue implements Initializable, CometTask {
    private static final Logger log = Logger.getLogger(ItemStorageQueue.class.getName());
    private static ItemStorageQueue instance;

    private ScheduledFuture future;

    private List<RoomItem> itemsToStoreData;
    private List<RoomItem> itemsToStore;

    public ItemStorageQueue() {
        // TODO: Multiple types of save tasks. (Position, placement, data etc.)
        this.itemsToStoreData = new CopyOnWriteArrayList<>();
        this.itemsToStore = new CopyOnWriteArrayList<>();
    }

    @Override
    public void initialize() {
        this.future = CometThreadManager.getInstance().executePeriodic(this, 0, 3000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if (this.itemsToStoreData.size() == 0 && this.itemsToStore.size() == 0) return;

        log.debug("Saving " + (this.itemsToStoreData.size() + this.itemsToStore.size()) + " items");

        RoomItemDao.processBatch(this.itemsToStoreData);
        RoomItemDao.saveFloorItems(this.itemsToStore);

        this.itemsToStoreData.clear();
    }

    public void queueSaveData(final RoomItem roomItem) {
        if (this.itemsToStoreData.contains(roomItem)) {
            this.itemsToStoreData.remove(roomItem);
        }

        this.itemsToStoreData.add(roomItem);
    }

    public void queueSave(final RoomItem roomItem) {
        if(this.itemsToStore.contains(roomItem)) {
            this.itemsToStore.remove(roomItem);
        }

        this.itemsToStore.add(roomItem);
    }

    public void shutdown() {
        this.future.cancel(false);

        log.info("Executing " + this.itemsToStoreData.size() + " item data updates");

        // Run 1 final time, to make sure everything is saved!
        this.run();
    }

    public static ItemStorageQueue getInstance() {
        if (instance == null) {
            instance = new ItemStorageQueue();
        }

        return instance;
    }

    public void unqueue(RoomItemFloor floorItem) {
        if(this.itemsToStore.contains(floorItem)) {
            this.itemsToStore.remove(floorItem);
        }
    }
}
