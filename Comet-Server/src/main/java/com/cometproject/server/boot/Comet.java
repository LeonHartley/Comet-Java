package com.cometproject.server.boot;

import com.cometproject.api.stats.CometStats;
import com.cometproject.server.boot.utils.ConsoleCommands;
import com.cometproject.server.boot.utils.ShutdownProcess;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.utilities.CometRuntime;
import com.cometproject.server.utilities.TimeSpan;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.*;


public class Comet {
    public static String instanceId = UUID.randomUUID().toString();
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
     * Whether or not we want to show the GUI
     */
    public static boolean showGui = false;
    /**
     * Whether we're running Comet in daemon mode or not
     */
    public static boolean daemon = false;
    /**
     * Logging during start-up & console commands
     */
    private static Logger log = Logger.getLogger(Comet.class.getName());
    /**
     * The main server instance
     */
    private static CometServer server;

    /**
     * Start the server!
     *
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

        log.info("  /$$$$$$                                      /$$    ");
        log.info(" /$$__  $$                                    | $$    ");
        log.info("| $$  \\__/  /$$$$$$  /$$$$$$/$$$$   /$$$$$$  /$$$$$$  ");
        log.info("| $$       /$$__  $$| $$_  $$_  $$ /$$__  $$|_  $$_/  ");
        log.info("| $$      | $$  \\ $$| $$ \\ $$ \\ $$| $$$$$$$$  | $$    ");
        log.info("| $$    $$| $$  | $$| $$ | $$ | $$| $$_____/  | $$ /$$");
        log.info("|  $$$$$$/|  $$$$$$/| $$ | $$ | $$|  $$$$$$$  |  $$$/");
        log.info(" \\______/  \\______/ |__/ |__/ |__/ \\_______/   \\___/  ");

        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (arg.contains("dt_")) {
                isDebugging = true;
                break;
            }
        }

        Level logLevel = Level.INFO;

        if (args.length < 1) {
            log.debug("No config args found, falling back to default configuration!");
            server = new CometServer(null);
        } else {
            Map<String, String> cometConfiguration = new HashMap<>();

            // Parse args
            List<String> arguments = new ArrayList<>();

            for (int i = 0; i < args.length; i++) {
                final String arg = args[i];

                if (arg.contains(" ")) {
                    final String[] splitString = arg.split(" ");

                    for (int j = 0; j < splitString.length; j++) {
                        arguments.add(splitString[j]);
                    }
                } else {
                    arguments.add(arg);
                }
            }

            for (String arg : arguments) {
                if (arg.equals("--debug-logging")) {
                    logLevel = Level.TRACE;
                }

                if (arg.equals("--gui")) {
                    // start GUI!
                    showGui = true;
                }

                if (arg.equals("--daemon")) {
                    daemon = true;
                }

                if (arg.startsWith("--instance-name=")) {
                    instanceId = arg.replace("--instance-name=", "");
                }

                if (!arg.contains("="))
                    continue;

                String[] splitArgs = arg.split("=");

                cometConfiguration.put(splitArgs[0], splitArgs.length != 1 ? splitArgs[1] : "");
            }

            server = new CometServer(cometConfiguration);
        }

        Logger.getRootLogger().setLevel(logLevel);
        server.init();

        if (!daemon) {
            ConsoleCommands.init();
        }

        ShutdownProcess.init();
    }

    /**
     * Exit the comet server
     *
     * @param message The message to display to the console
     */
    public static void exit(String message) {
        log.error("Comet has shutdown. Reason: \"" + message + "\"");
        System.exit(0);
    }

    /**
     * Get the instance time in seconds
     *
     * @return The time in seconds
     */
    public static long getTime() {
        return (System.currentTimeMillis() / 1000L);
    }

    /**
     * Get the instance date [HH:MM:SS]
     *
     * @return The date
     */
    public static String getDate() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    /**
     * Get the instance build of Comet
     *
     * @return The instance build of Comet
     */
    public static String getBuild() {
        return Comet.class.getPackage().getImplementationVersion() == null ? "Comet-DEV" : Comet.class.getPackage().getImplementationVersion();
    }

    /**
     * Gets the instance server stats
     *
     * @return Server stats object
     */
    public static CometStats getStats() {
        CometStats statsInstance = new CometStats();

        statsInstance.setPlayers(NetworkManager.getInstance().getSessions().getUsersOnlineCount());
        statsInstance.setRooms(RoomManager.getInstance().getRoomInstances().size());
        statsInstance.setUptime(TimeSpan.millisecondsToDate(System.currentTimeMillis() - Comet.start));

        statsInstance.setProcessId(CometRuntime.processId);
        statsInstance.setAllocatedMemory((Runtime.getRuntime().totalMemory() / 1024) / 1024);
        statsInstance.setUsedMemory(statsInstance.getAllocatedMemory() - (Runtime.getRuntime().freeMemory() / 1024) / 1024);
        statsInstance.setOperatingSystem(CometRuntime.operatingSystem + " (" + CometRuntime.operatingSystemArchitecture + ")");
        statsInstance.setCpuCores(Runtime.getRuntime().availableProcessors());

        return statsInstance;
    }

    /**
     * Get the main server instance
     *
     * @return The main server instance
     */
    public static CometServer getServer() {
        return server;
    }
}
