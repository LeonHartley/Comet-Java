package com.cometproject.storage.api;

import com.cometproject.api.config.Configuration;
import sun.security.krb5.Config;

public interface IStorageInitializer {

    void setup(StorageContext storageContext);

}
