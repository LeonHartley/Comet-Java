package com.cometproject.server.boot.utils;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.utilities.CometStats;
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
                                    final CometStats stats = CometStats.get();

                                    log.info("This server is powered by Comet (" + Comet.getBuild() + ")");
                                    log.info("    Users online: " + stats.getPlayers());
                                    log.info("    Rooms loaded: " + stats.getRooms());
                                    log.info("    Uptime: " + stats.getUptime());
                                    log.info("    Process ID: " + stats.getProcessId());
                                    log.info("    Memory allocated: " + stats.getAllocatedMemory() + "MB");
                                    log.info("    Memory usage: " + stats.getUsedMemory() + "MB");
                                    break;

                                case "/reload_messages":
                                    NetworkManager.getInstance().getMessages().load();
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
                                    PermissionsManager.getInstance().loadCommands();
                                    PermissionsManager.getInstance().loadPerks();
                                    PermissionsManager.getInstance().loadPermissions();
                                    log.info("Permissions cache was reloaded.");
                                    break;

                                case "/reload_catalog":
                                    CatalogManager.getInstance().loadPages();
                                    CatalogManager.getInstance().loadGiftBoxes();
                                    log.info("Catalog cache was reloaded.");
                                    break;

                                case "/reload_bans":
                                    BanManager.getInstance().loadBans();
                                    log.info("Bans were reloaded.");
                                    break;

                                case "/reload_navigator":
                                    NavigatorManager.getInstance().loadFeaturedRooms();
                                    NavigatorManager.getInstance().loadCategories();
                                    log.info("Navigator was reloaded.");
                                    break;

                                case "/reload_locale":
                                    Locale.initialize();
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
