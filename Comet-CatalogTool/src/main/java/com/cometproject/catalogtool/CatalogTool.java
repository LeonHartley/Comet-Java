package com.cometproject.catalogtool;

import com.cometproject.server.boot.Comet;
import com.cometproject.api.config.Configuration;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.storage.StorageManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;

public class CatalogTool {
    private static Logger log = Logger.getLogger(Comet.class.getName());

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Invalid arguments, expecting pageId and furniline");
        }

        final int pageId = Integer.parseInt(args[0]);
        final String furniline = args[1];

        Configuration.setConfiguration(new Configuration("./config/comet.properties"));

        try {
            PropertyConfigurator.configure(new FileInputStream("./config/log4j.properties"));
        } catch (Exception e) {
            log.error("Error while loading log4j configuration", e);
            return;
        }

        StorageManager.getInstance().initialize();
        ItemManager.getInstance().initialize();
        CatalogManager.getInstance().initialize();

        //parse xml
        // loop through every item with furniline = furniline and create definitions where needed
        // and create catalog items
    }
}
