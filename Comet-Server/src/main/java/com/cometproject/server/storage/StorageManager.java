package com.cometproject.server.storage;

import com.cometproject.server.storage.cache.CacheManager;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.storage.connections.HikariConnectionProvider;
import com.cometproject.storage.mysql.StorageContext;

public class StorageManager implements Initialisable {
    private static StorageManager storageManagerInstance;

    private final HikariConnectionProvider hikariConnectionProvider;

    public StorageManager() {
        hikariConnectionProvider = new HikariConnectionProvider();
    }

    @Override
    public void initialize() {

        final StorageContext storageContext = new StorageContext(hikariConnectionProvider);
        StorageContext.setCurrent(storageContext);

        CacheManager.getInstance().initialize();
        SqlHelper.init(hikariConnectionProvider);
    }

    public static StorageManager getInstance() {
        if (storageManagerInstance == null)
            storageManagerInstance = new StorageManager();

        return storageManagerInstance;
    }

    public void shutdown() {
        this.hikariConnectionProvider.shutdown();
    }
}
