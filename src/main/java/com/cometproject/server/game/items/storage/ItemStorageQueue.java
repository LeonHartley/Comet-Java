package com.cometproject.server.game.items.storage;

import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class ItemStorageQueue implements Initializable, CometTask {
    private static final Logger log = Logger.getLogger(ItemStorageQueue.class.getName());
    private static ItemStorageQueue instance;

    private List<RoomItem> itemsToStore;


    public ItemStorageQueue() {
        this.itemsToStore = new CopyOnWriteArrayList<>();
    }

    @Override
    public void initialize() {
        CometThreadManager.getInstance().executePeriodic(this, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if(this.itemsToStore.size() == 0) return;

        log.debug("Saving " + this.itemsToStore.size() + " items");

        RoomItemDao.processBatch(this.itemsToStore);
        this.itemsToStore.clear();
    }

    public void queueSaveData(final RoomItem roomItem) {
        if(this.itemsToStore.contains(roomItem)) {
            this.itemsToStore.remove(roomItem);
        }

        this.itemsToStore.add(roomItem);
    }

    public static ItemStorageQueue getInstance() {
        if(instance == null) {
            instance = new ItemStorageQueue();
        }

        return instance;
    }
}
