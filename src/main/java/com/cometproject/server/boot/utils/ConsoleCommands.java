package com.cometproject.server.boot.utils;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleCommands {
    private static final Logger log = Logger.getLogger("Console Command Handler");

    public static void init() {
        // Console commands
        final Thread cmdThr = new Thread() {
            public void run() {
                while (Comet.isRunning) {
                    if (!Comet.isRunning) {
                        break;
                    }

                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        String input = br.readLine();

                        if (input != null && input.startsWith("/")) {
                            switch (input.split(" ")[0]) {
                                default:
                                    log.error("Invalid command");
                                    break;
                                case "/":
                                    log.info("Commands available: /about, /reload_messages, /gc, /reload_permissions, /changemotd, /reload_catalog, /reload_bans, /reload_locale, /reload_permissions");
                                    break;

                                case "/about":
                                    log.info("This server is powered by Comet (" + Comet.getBuild() + ")");
                                    log.info("    Users online: " + Comet.getServer().getNetwork().getSessions().getUsersOnlineCount());
                                    log.info("    Rooms online: " + CometManager.getRooms().getRoomInstances().size());
                                    break;

                                case "/reload_messages":
                                    Comet.getServer().getNetwork().getMessages().load();
                                    log.info("Message handlers were reloaded");
                                    break;

                                case "/gc":
                                    System.gc();
                                    log.info("GC was run");
                                    break;

                                case "/changemotd":
                                    String motd = input.replace("/changemotd ", "");
                                    CometSettings.setMotd(motd);
                                    log.info("Message of the day was set.");
                                    break;

                                case "/reload_permissions":
                                    CometManager.getPermissions().loadCommands();
                                    CometManager.getPermissions().loadPerks();
                                    CometManager.getPermissions().loadPermissions();
                                    log.info("Permissions cache was reloaded.");
                                    break;

                                case "/reload_catalog":
                                    CometManager.getCatalog().loadPages();
                                    log.info("Catalog cache was reloaded.");
                                    break;

                                case "/reload_bans":
                                    CometManager.getBans().loadBans();
                                    log.info("Bans were reloaded.");
                                    break;

                                case "/reload_navigator":
                                    CometManager.getNavigator().loadFeaturedRooms();
                                    CometManager.getNavigator().loadCategories();
                                    log.info("Navigator was reloaded.");
                                    break;

                                case "/reload_locale":
                                    Locale.init();
                                    log.info("Locale configuration was reloaded.");
                                    break;

                            }
                        } else {
                            log.error("Invalid command");
                        }
                    } catch (Exception e) {
                        log.error("Error while parsing console command");
                    }
                }
            }
        };

        cmdThr.start();
    }
}
