package com.cometproject.server.boot;

import com.cometproject.server.boot.utils.ConsoleCommands;
import com.cometproject.server.boot.utils.ShutdownHook;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.storage.queries.system.StatisticsDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Map;

public class Comet {
    /**
     * Logging during start-up & console commands
     */
    private static Logger log = Logger.getLogger(Comet.class.getName());

    /**
     * The main server instance
     */
    private static CometServer server;

    /**
     * The time the server was started
     */
    public static long start;

    /**
     * Is a debugger attached?
     */
    public static volatile boolean isDebugging = false;

    /**
     * Is Comet running?
     */
    public static volatile boolean isRunning = true;

    /**
     * Start the server!
     * @param args The arguments passed from the run command
     */
    public static void run(String[] args) {
        start = System.currentTimeMillis();

        try {
            PropertyConfigurator.configure(new FileInputStream("./config/log4j.properties"));
        } catch (Exception e) {
            log.error("Error while loading log4j configuration", e);
            return;
        }

        log.info("Comet Server - " + getBuild());

        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (arg.contains("dt_")) {
                isDebugging = true;
                break;
            }
        }

        server = new CometServer();

        if (args.length < 1) {
            log.warn("No config args found, falling back to default configuration!");
            server.init();
        } else {
            Map<String, String> cometConfiguration = new FastMap<>();

            for (int i = 0; i < args.length; i++) {
                cometConfiguration.put(args[i].split("=")[0], args[i].split("=")[1]);
            }

            server.init(cometConfiguration);
        }

        ConsoleCommands.init();
        ShutdownHook.init();
    }

    /**
     * Exit the comet server
     * @param message The message to display to the console
     */
    public static void exit(String message) {
        log.error("Comet has shutdown. Reason: \"" + message + "\"");
        System.exit(0);
    }

    /**
     * Get the current time in seconds
     * @return The time in seconds
     */
    public static long getTime() {
        return (System.currentTimeMillis() / 1000L);
    }

    /**
     * Get the current build of Comet
     * @return The current build of Comet
     */
    public static String getBuild() {
        return "0.9.1-ALPHA1";
    }

    /**
     * Get the main server instance
     * @return The main server instance
     */
    public static CometServer getServer() {
        return server;
    }
}