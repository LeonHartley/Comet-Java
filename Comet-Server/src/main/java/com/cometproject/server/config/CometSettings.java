package com.cometproject.server.config;

import com.cometproject.server.game.rooms.filter.FilterMode;
import com.cometproject.server.storage.queries.config.ConfigDao;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.util.Map;


public class CometSettings {
    /**
     * Is the login message enabled?
     */
    public static boolean messageOfTheDayEnabled = false;

    /**
     * If the login message is enabled, this message
     * will be shown.
     */
    public static String messageOfTheDayText = "";

    /**
     * The log store type - default is disabled
     */
    public static String logStore = "disabled";

    /**
     * The name of the hotel... Will be shown in the
     * footer of alerts etc.
     */
    public static String hotelName = "";

    /**
     * The URL to the hotel... If you click the hotel name in
     * an alert, it will take you to this link.
     */
    public static String hotelUrl = "";

    /**
     * The image which will be displayed in the about command
     */
    public static String aboutImg = "";

    /**
     * The cost of groups (currently only credits.. will be changed in the future...)
     */
    public static int groupCost = 0;

    /**
     * Do users get credits every 15 minutes?
     */
    public static boolean quarterlyCreditsEnabled = false;

    /**
     * If enabled, the players will get x amount of credits
     * every 15 minutes
     */
    public static int quarterlyCreditsAmount = 0;

    /**
     * Do users get duckets every 15 minutes?
     */
    public static boolean quarterlyDucketsEnabled = false;

    /**
     * If enabled, the players will get x amount of duckets
     * every 15 minutes
     */
    public static int quarterlyDucketsAmount = 0;

    /**
     * Do you want to show how many players are online in the
     * about command window?
     */
    public static boolean showUsersOnlineInAbout = true;

    /**
     * Do you want to show the server uptime in the about
     * command window?
     */
    public static boolean showUptimeInAbout = true;

    /**
     * Do you want to show how many active rooms there are
     * in the about command window?
     */
    public static boolean showActiveRoomsInAbout = true;

    /**
     * The max amount of tiles on the X axis a player can use in their
     * custom floor plan (:floor)
     */
    public static int floorMaxX = 0;

    /**
     * The max amount of tiles on the Y axis a player can use in their
     * custom floor plan (:floor)
     */
    public static int floorMaxY = 0;

    /**
     * The max amount of tiles (X*Y) a player can use in their custom floor
     * plan (:floor)
     */
    public static int floorMaxTotal = 0;

    /**
     * The max amount of players allowed to non-staff members in a room (the
     * limit that shows in the room settings window)
     */
    public static int maxPlayersInRoom = 150;

    /**
     * Is room password encryption enabled?
     */
    public static boolean roomPasswordEncryptionEnabled = false;

    /**
     * How many rounds of encryption do you want to use for
     * room password encryption (if enabled)
     */
    public static int roomPasswordEncryptionRounds = 10;

    /**
     * The mode in which the word filter will be in. If the
     * mode is in strict, it will filter out accents and also
     * block messages which match against the trigger words
     */
    public static FilterMode wordFilterMode = FilterMode.DEFAULT;

    /**
     * If this is enabled, it will use the IP from the database and not from the client
     */
    public static boolean useDatabaseIp = false;

    /**
     * If enabled, all logins will be saved to the database
     */
    public static boolean storeAccess = false;

    /**
     * If disable, the wired items will not send the flash update
     */
    public static boolean disableWiredFlash = false;

    /**
     * If enabled, the player will have infinite credits & duckets.
     */
    public static boolean infiniteBalance = false;

    /**
     * The amount of seconds players need to wait until they can send another gift.
     */
    public static int giftCooldown = 30;

    /**
     * Is the item storage queue enabled?
     */
    public static boolean itemStorageQueueEnabled = false;

    /**
     * Filter characters... These are used to replace custom
     * characters from user messages to be parsed by the filter.
     */
    public static final Map<String, String> strictFilterCharacters = Maps.newHashMap();

    /**
     * Whether or not we're allowed to place furniture on room entities or not.
     */
    public static boolean placeItemOnEntity = false;

    /**
     * Calculate entity process delays on the fly, rather than using a set in stone 500ms
     */
    public static boolean adaptiveEntityProcessDelay = false;

    /**
     *
     */
    public static int playerFigureUpdateTimeout = 5;

    public static int maxBotsInRoom = 15;

    public static int maxFriends = 1100;

    public static boolean logMessengerMessages = false;

    /**
     * Logging
     */
    private static final Logger log = Logger.getLogger(CometSettings.class.getName());

    /**
     * Initialize the configuration
     */
    public static void initialize() {
        Map<String, String> config = ConfigDao.getAll();

        log.info("Loaded " + config.size() + " config strings");

        messageOfTheDayEnabled = Boolean.parseBoolean(config.get("comet.game.motd.enabled"));
        messageOfTheDayText = config.get("comet.game.motd.text");

        hotelName = config.get("comet.game.hotel.name");
        hotelUrl = config.get("comet.game.hotel.url");
        aboutImg = config.get("comet.game.about.img");

        groupCost = Integer.parseInt(config.get("comet.game.groups.cost"));

        quarterlyCreditsEnabled = Boolean.parseBoolean(config.get("comet.game.quarterly.credits.enabled"));
        quarterlyCreditsAmount = Integer.parseInt(config.get("comet.game.quarterly.credits.amount"));

        quarterlyDucketsEnabled = Boolean.parseBoolean(config.get("comet.game.quarterly.duckets.enabled"));
        quarterlyDucketsAmount = Integer.parseInt(config.get("comet.game.quarterly.duckets.amount"));

        showUptimeInAbout = Boolean.parseBoolean(config.get("comet.about.command.uptime"));
        showActiveRoomsInAbout = Boolean.parseBoolean(config.get("comet.about.command.activeRooms"));
        showUsersOnlineInAbout = Boolean.parseBoolean(config.get("comet.about.command.usersOnline"));

        if (config.containsKey("comet.floor.command.max.x") && config.containsKey("comet.floor.command.max.y") && config.containsKey("comet.floor.command.max.total")) {
            floorMaxX = Integer.parseInt(config.get("comet.floor.command.max.x"));
            floorMaxY = Integer.parseInt(config.get("comet.floor.command.max.y"));
            floorMaxTotal = Integer.parseInt(config.get("comet.floor.command.max.total"));
        }

        if (config.containsKey("comet.game.rooms.maxPlayers")) {
            maxPlayersInRoom = Integer.parseInt(config.get("comet.game.rooms.maxPlayers"));
        }

        if (config.containsKey("comet.game.rooms.hashpasswords") && config.containsKey("comet.game.rooms.hashrounds")) {
            roomPasswordEncryptionEnabled = Boolean.parseBoolean(config.get("comet.game.rooms.hashpasswords"));
            roomPasswordEncryptionRounds = Integer.parseInt(config.get("comet.game.rooms.hashrounds"));
        }

        if (config.containsKey("comet.game.filter.mode")) {
            wordFilterMode = FilterMode.valueOf(config.get("comet.game.filter.mode").toUpperCase());
        }

        if (config.containsKey("config.security.use_database_ip")) {
            useDatabaseIp = Boolean.parseBoolean(config.get("config.security.use_database_ip"));
        }

        if (config.containsKey("comet.security.storeAccess")) {
            storeAccess = Boolean.parseBoolean(config.get("comet.security.storeAccess"));
        }

        if (config.containsKey("comet.game.rooms.disableWiredFlash")) {
            disableWiredFlash = Boolean.parseBoolean(config.get("comet.game.rooms.disableWiredFlash"));
        }

        if (config.containsKey("comet.game.infiniteBalance")) {
            infiniteBalance = Boolean.parseBoolean(config.get("comet.game.infiniteBalance"));
        }

        if (config.containsKey("comet.game.gift.cooldown")) {
            giftCooldown = Integer.parseInt(config.get("comet.game.gift.cooldown"));
        }

        if (config.containsKey("comet.data.itemStorageQueue")) {
            itemStorageQueueEnabled = Boolean.parseBoolean(config.get("comet.data.itemStorageQueue"));
        }

        if (config.containsKey("comet.game.filter.characters")) {
            strictFilterCharacters.clear();

            final String characters = config.get("comet.game.filter.characters");

            for (String charSet : characters.split(",")) {
                if (!charSet.contains(":")) continue;

                final String[] chars = charSet.split(":");

                if (chars.length == 2) {
                    strictFilterCharacters.put(chars[0], chars[1]);
                } else {
                    strictFilterCharacters.put(chars[0], "");
                }
            }
        }

        if (config.containsKey("comet.game.furni.placeOnPlayer")) {
            placeItemOnEntity = Boolean.parseBoolean(config.get("comet.game.furni.placeOnPlayer"));
        }

        if (config.containsKey("comet.game.figureUpdateTimeout")) {
            playerFigureUpdateTimeout = Integer.parseInt(config.get("comet.game.figureUpdateTimeout"));
        }

        if (config.containsKey("comet.game.bots.maxBotsInRoom")) {
            maxBotsInRoom = Integer.parseInt(config.get("comet.game.bots.maxBotsInRoom"));
        }

        if (config.containsKey("comet.game.messenger.maxFriends")) {
            maxFriends = Integer.parseInt(config.get("comet.game.messenger.maxFriends"));
        }

        if (config.containsKey("comet.game.messenger.logMessages")) {
            logMessengerMessages = Boolean.parseBoolean(config.get("comet.game.messenger.logMessages"));
        }
    }

    /**
     * Enable & set the Message Of The Day text
     *
     * @param motd The message to display to the user on-login
     */
    public static void setMotd(String motd) {
        messageOfTheDayEnabled = true;
        messageOfTheDayText = motd;
    }
}
