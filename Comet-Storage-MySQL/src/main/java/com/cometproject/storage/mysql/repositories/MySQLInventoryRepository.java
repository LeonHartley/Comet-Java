package com.cometproject.storage.mysql.repositories;

import com.cometproject.storage.api.repositories.IInventoryRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;

public class MySQLInventoryRepository extends MySQLRepository implements IInventoryRepository {
    public MySQLInventoryRepository(MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);
    }




}
