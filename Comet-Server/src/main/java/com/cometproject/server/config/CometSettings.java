package com.cometproject.server.config;

import com.cometproject.server.game.rooms.filter.FilterMode;
import com.cometproject.server.storage.queries.config.ConfigDao;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.util.Map;


public class CometSettings {
    public static boolean motdEnabled = false;
    public static String motdMessage = "";
    public static String hotelName = "";
    public static String hotelUrl = "";
    public static String aboutImg = "";

    public static boolean onlineRewardEnabled = false;
    public static int onlineRewardCredits = 0;
    public static int onlineRewardDuckets = 0;
    public static int onlineRewardInterval = 15;

    public static int groupCost = 0;

    public static boolean aboutShowPlayersOnline = true;
    public static boolean aboutShowUptime = true;
    public static boolean aboutShowRoomsActive = true;

    public static int floorEditorMaxX = 0;
    public static int floorEditorMaxY = 0;
    public static int floorEditorMaxTotal = 0;

    public static int roomMaxPlayers = 150;
    public static boolean roomEncryptPasswords = false;
    public static int roomPasswordEncryptionRounds = 10;
    public static boolean roomCanPlaceItemOnEntity = false;
    public static int roomMaxBots = 15;
    public static int roomMaxPets = 15;
    public static int roomIdleMinutes = 20;

    public static FilterMode wordFilterMode = FilterMode.DEFAULT;

    public static boolean useDatabaseIp = false;
    public static boolean saveLogins = false;

    public static boolean playerInfiniteBalance = false;
    public static int playerGiftCooldown = 30;

    public static final Map<String, String> strictFilterCharacters = Maps.newHashMap();
    public static boolean playerFigureValidation = false;

    public static int playerChangeFigureCooldown = 5;
    public static int messengerMaxFriends = 1100;

    public static boolean messengerLogMessages = false;
    public static int cameraPhotoItemId = 50001;

    public static String cameraPhotoUrl = "";
    public static int roomWiredRewardMinimumRank = 7;
    public static boolean asyncCatalogPurchase = false;

    public static boolean storagePlayerQueueEnabled = false;
    public static boolean storageItemQueueEnabled = false;

    public static boolean adaptiveEntityProcessDelay = false;

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
        motdEnabled = true;
        motdMessage = motd;
    }
}
