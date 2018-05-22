package com.cometproject.server.storage;

import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.storage.cache.CacheManager;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.mysql.MySQLStorageInitializer;
import com.cometproject.storage.mysql.connections.HikariConnectionProvider;

public class StorageManager implements Initialisable {
    private static StorageManager storageManagerInstance;

    private final HikariConnectionProvider hikariConnectionProvider;

    public StorageManager() {
        hikariConnectionProvider = new HikariConnectionProvider();
    }

    public static StorageManager getInstance() {
        if (storageManagerInstance == null)
            storageManagerInstance = new StorageManager();

        return storageManagerInstance;
    }

    @Override
    public void initialize() {
        final MySQLStorageInitializer initializer = new MySQLStorageInitializer(hikariConnectionProvider);
        final StorageContext storageContext = new StorageContext();

        initializer.setup(storageContext);

        StorageContext.setCurrentContext(storageContext);

        CacheManager.getInstance().initialize();
        SqlHelper.init(hikariConnectionProvider);
    }

    public void shutdown() {
        this.hikariConnectionProvider.shutdown();
    }
}
