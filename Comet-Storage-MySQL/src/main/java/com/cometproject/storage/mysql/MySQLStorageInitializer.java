package com.cometproject.storage.mysql;

import com.cometproject.storage.api.IStorageInitializer;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.mysql.models.factories.GroupDataFactory;
import com.cometproject.storage.mysql.models.factories.GroupForumMessageFactory;
import com.cometproject.storage.mysql.models.factories.GroupForumSettingsFactory;
import com.cometproject.storage.mysql.models.factories.GroupMemberFactory;
import com.cometproject.storage.mysql.models.landing.factories.HallOfFameFactory;
import com.cometproject.storage.mysql.models.landing.factories.PromoArticleFactory;
import com.cometproject.storage.mysql.repositories.groups.MySQLGroupForumRepository;
import com.cometproject.storage.mysql.repositories.groups.MySQLGroupMemberRepository;
import com.cometproject.storage.mysql.repositories.groups.MySQLGroupRepository;
import com.cometproject.storage.mysql.repositories.landing.MySQLHallOfFameRepository;
import com.cometproject.storage.mysql.repositories.landing.MySQLPromoArticleRepository;

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
        storageContext.setPromoArticleRepository(new MySQLPromoArticleRepository(new PromoArticleFactory(), connectionProvider));
        storageContext.setHallOfFameRepository(new MySQLHallOfFameRepository(new HallOfFameFactory(), connectionProvider));
    }
}
