package com.cometproject.server.boot;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

public class Comet {
    private static Logger log = Logger.getLogger(Comet.class.getName());
    private static CometServer server = new CometServer();
    public static long start;

    public static boolean isDebugging = false;
    public static volatile boolean isRunning = true;

    public static void main(String[] args) {
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

        server.init();

        // Console commands
        Thread cmdThr = new Thread() {
            public void run() {
                while (isRunning) {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        String input = br.readLine();

                        if (input != null && input.startsWith("/")) {
                            switch (input.split(" ")[0]) {
                                default:
                                    Comet.log.error("Invalid command");
                                    break;
                                case "/":
                                    Comet.log.info("Commands available: /about, /reload_messages, /gc, /reload_permissions, /changemotd, /reload_catalog, /reload_bans, /reload_locale, /reload_permissions");
                                    break;

                                case "/about":
                                    Comet.log.info("This server is powered by Comet (" + Comet.getBuild() + ")");
                                    Comet.log.info("    Users online: " + Comet.getServer().getNetwork().getSessions().getUsersOnlineCount());
                                    Comet.log.info("    Rooms online: " + GameEngine.getRooms().getActiveRooms().size());
                                    break;

                                case "/reload_messages":
                                    Comet.getServer().getNetwork().getMessages().load();
                                    Comet.log.info("Message handlers were reloaded");
                                    break;

                                case "/gc":
                                    System.gc();
                                    Comet.log.info("GC was run");
                                    break;

                                case "/changemotd":
                                    String motd = input.replace("/changemotd ", "");
                                    CometSettings.setMotd(motd);
                                    Comet.log.info("Message of the day was set.");
                                    break;

                                case "/reload_permissions":
                                    GameEngine.getPermissions().loadCommands();
                                    GameEngine.getPermissions().loadPerks();
                                    GameEngine.getPermissions().loadPermissions();
                                    Comet.log.info("Permissions cache was reloaded.");
                                    break;

                                case "/reload_catalog":
                                    GameEngine.getCatalog().loadPages();
                                    Comet.log.info("Catalog cache was reloaded.");
                                    break;

                                case "/reload_bans":
                                    GameEngine.getBans().loadBans();
                                    Comet.log.info("Bans were reloaded.");
                                    break;

                                case "/reload_navigator":
                                    GameEngine.getNavigator().loadFeaturedRooms();
                                    GameEngine.getNavigator().loadCategories();
                                    Comet.log.info("Navigator was reloaded.");
                                    break;

                                case "/reload_locale":
                                    Locale.init();
                                    Comet.log.info("Locale configuration was reloaded.");
                                    break;

                            }
                        } else {
                            Comet.log.error("Invalid command");
                        }
                    } catch (Exception e) {
                        Comet.log.error("Error while parsing console command");
                    }
                }
            }
        };

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                isRunning = false;
            }
        });

        cmdThr.start();
    }

    public static void exit(String message) {
        log.error("Comet has shutdown. Reason: \"" + message + "\"");
        System.exit(0);
    }

    public static long getTime() {
        return (System.currentTimeMillis() / 1000L);
    }

    public static String getBuild() {
        // Soon, there will be a connection to the "master server"
        // which will give warnings on the console
        // that the version is out of date (if its true)
        //
        // Version guide
        // ===============================
        // 0.x = Pre-beta version
        // 0.4.x = Alpha
        // 1.x = Post-beta (release)
        // Letters: a = release candidate for point update (0.x, 1.x) - unstable
        //          b = if there was an update just before letter A was distributed, b will be appended
        //          c = Pre-release  -- must be mostly stable/usable
        //          no letter = release!  -- must be stable
        return "0.7.2a";
    }

    public static CometServer getServer() {
        return server;
    }
}