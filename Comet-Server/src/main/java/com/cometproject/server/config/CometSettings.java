package com.cometproject.server.config;

import com.cometproject.server.game.rooms.filter.FilterMode;
import com.cometproject.server.storage.queries.config.ConfigDao;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.util.Map;


public class CometSettings {
    public static boolean messageOfTheDayEnabled = false;
    public static String messageOfTheDayText = "";
    public static String hotelName = "";
    public static String hotelUrl = "";
    public static String aboutImg = "";

    public static boolean onlineRewardEnabled = false;
    public static int onlineRewardCredits = 0;
    public static int onlineRewardDuckets = 0;
    public static int onlineRewardInterval = 15;

    public static int groupCost = 0;

    public static boolean showUsersOnlineInAbout = true;
    public static boolean showUptimeInAbout = true;
    public static boolean showActiveRoomsInAbout = true;

    public static int floorMaxX = 0;
    public static int floorMaxY = 0;
    public static int floorMaxTotal = 0;
    public static int maxPlayersInRoom = 150;
    public static boolean roomPasswordEncryptionEnabled = false;
    public static int roomPasswordEncryptionRounds = 10;
    public static FilterMode wordFilterMode = FilterMode.DEFAULT;
    public static boolean useDatabaseIp = false;
    public static boolean storeAccess = false;
    public static boolean infiniteBalance = false;
    public static int giftCooldown = 30;
    public static boolean itemStorageQueueEnabled = false;
    public static final Map<String, String> strictFilterCharacters = Maps.newHashMap();
    public static boolean placeItemOnEntity = false;
    public static boolean adaptiveEntityProcessDelay = false;
    public static int playerFigureUpdateTimeout = 5;
    public static int maxBotsInRoom = 15;
    public static int maxPetsInRoom = 15;
    public static int maxFriends = 1100;
    public static boolean logMessengerMessages = false;
    public static int cameraPhotoItemId = 50001;
    public static String cameraPhotoUrl = "";
    public static int wiredRewardMinRank = 7;
    public static boolean asyncCatalogPurchase = false;
    public static int idleMinutes = 20;
    public static boolean playerDataStorageQueue = false;
    public static boolean figureValidation = false;

    private static final Logger log = Logger.getLogger(CometSettings.class.getName());

    /**
     * Initialize the configuration
     */
    public static void initialize() {
        ConfigDao.getAll();
        log.info("Initialized configuration");
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
