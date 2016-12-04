package com.cometproject.website.boot;

import com.cometproject.website.config.Configuration;
import com.cometproject.website.storage.SqlStorageManager;
import com.cometproject.website.website.WebsiteManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;

public class CometWebsite {
    private final SqlStorageManager sqlStorageManager;
    private final WebsiteManager websiteManager;

    private final Logger log = Logger.getLogger(CometWebsite.class.getName());

    public CometWebsite() {
        this.configureLogging();

        this.sqlStorageManager = new SqlStorageManager();
        this.websiteManager = new WebsiteManager();
    }

    private boolean configureLogging() {
        try {
            PropertyConfigurator.configure(new FileInputStream("./config/log4j.properties"));
        } catch(Exception e) {
            System.out.println("Error while configuring log4j");
            return false;
        }

        return true;
    }

    private static String[] arguments;
    private static CometWebsite cometManager;

    public static void main(String[] args) {
        arguments = args;
        cometManager = new CometWebsite();
    }

    public static String[] getArguments() {
        return arguments;
    }

    public static CometWebsite getInstance() {
        return cometManager;
    }
}
