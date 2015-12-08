package com.cometproject.server.game.rooms.bundles;

import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.server.storage.queries.rooms.BundleDao;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomBundleManager {
    private static RoomBundleManager roomBundleManager;
    private static Logger log = Logger.getLogger(RoomBundleManager.class.getName());

    private Map<Integer, RoomBundle> bundles;

    public RoomBundleManager() {
        this.bundles = new ConcurrentHashMap<>();
    }

    public void initialize() {
        if(this.bundles.size() != 0) {
            this.bundles.clear();
        }

        BundleDao.loadActiveBundles(this.bundles);
        log.info("Loaded " + this.bundles.size() + " active room bundles");

        log.info("RoomBundleManager initialized");
    }

    public void addBundle(RoomBundle bundle) {
        this.bundles.put(bundle.getId(), bundle);
    }

    public RoomBundle getBundle(int id) {
        return this.bundles.get(id);
    }

    public static RoomBundleManager getInstance() {
        if(roomBundleManager == null) {
            roomBundleManager = new RoomBundleManager();
        }

        return roomBundleManager;
    }
}
