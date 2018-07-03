package com.cometproject.storage.mysql;

import com.cometproject.api.game.GameContext;
import com.cometproject.game.items.inventory.InventoryItemFactory;
import com.cometproject.storage.api.IStorageInitializer;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.mysql.models.factories.GroupDataFactory;
import com.cometproject.storage.mysql.models.factories.GroupForumMessageFactory;
import com.cometproject.storage.mysql.models.factories.GroupForumSettingsFactory;
import com.cometproject.storage.mysql.models.factories.GroupMemberFactory;
import com.cometproject.storage.mysql.models.factories.rooms.RoomDataFactory;
import com.cometproject.storage.mysql.models.factories.rooms.RoomModelDataFactory;
import com.cometproject.storage.mysql.repositories.*;

public class MySQLStorageInitializer implements IStorageInitializer {

    private final MySQLConnectionProvider connectionProvider;

    public MySQLStorageInitializer(MySQLConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;

        // Enables creation of MySQL repositories from outside the MySQL module :-)
        MySQLStorageContext.setCurrentContext(new MySQLStorageContext(connectionProvider));
    }

    @Override
    public void setup(StorageContext storageContext) {
        storageContext.setGroupRepository(new MySQLGroupRepository(new GroupDataFactory(), connectionProvider));
        storageContext.setGroupMemberRepository(new MySQLGroupMemberRepository(new GroupMemberFactory(), connectionProvider));
        storageContext.setGroupForumRepository(new MySQLGroupForumRepository(new GroupForumSettingsFactory(), new GroupForumMessageFactory(), connectionProvider));

        storageContext.setInventoryRepository(new MySQLInventoryRepository(new InventoryItemFactory(), connectionProvider));
        storageContext.setRoomItemRepository(new MySQLRoomItemRepository(connectionProvider));
        storageContext.setRoomRepository(new MySQLRoomRepository(new RoomDataFactory(), new RoomModelDataFactory(), connectionProvider));
        storageContext.setRewardRepository(new MySQLRewardRepository(connectionProvider));
        storageContext.setPhotoRepository(new MySQLPhotoRepository(connectionProvider));
    }
}
