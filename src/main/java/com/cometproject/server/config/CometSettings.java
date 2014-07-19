package com.cometproject.server.config;

import com.cometproject.server.game.rooms.filter.FilterMode;
import com.cometproject.server.storage.queries.config.ConfigDao;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class CometSettings {
    public static boolean httpEnabled = false;

    public static boolean messageOfTheDayEnabled = true;
    public static String messageOfTheDayText = "";

    public static String logStore = "disabled";

    public static String hotelName = "";
    public static String hotelUrl = "";
    public static String aboutImg = "";

    public static int groupCost = 0;

    public static boolean quartlyCreditsEnabled = false;
    public static int quartlyCreditsAmount = 0;

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

    // TODO: Catch missing-config exceptions and fallback to the defaults...
    public static void init() {
        Map<String, String> config = ConfigDao.getAll();

        messageOfTheDayEnabled = Boolean.parseBoolean(config.get("comet.game.motd.enabled"));
        messageOfTheDayText = config.get("comet.game.motd.text");

        hotelName = config.get("comet.game.hotel.name");
        hotelUrl = config.get("comet.game.hotel.url");
        aboutImg = config.get("comet.game.about.img");

        groupCost = Integer.parseInt(config.get("comet.game.groups.cost"));

        quartlyCreditsEnabled = Boolean.parseBoolean(config.get("comet.game.quarterly.credits.enabled"));
        quartlyCreditsAmount = Integer.parseInt(config.get("comet.game.quarterly.credits.amount"));

        showUptimeInAbout = Boolean.parseBoolean(config.get("comet.about.command.uptime"));
        showActiveRoomsInAbout = Boolean.parseBoolean(config.get("comet.about.command.activeRooms"));
        showUsersOnlineInAbout = Boolean.parseBoolean(config.get("comet.about.command.usersOnline"));

        floorMaxX = Integer.parseInt(config.get("comet.floor.command.max.x"));
        floorMaxY = Integer.parseInt(config.get("comet.floor.command.max.y"));
        floorMaxTotal = Integer.parseInt(config.get("comet.floor.command.max.total"));

        maxPlayersInRoom = Integer.parseInt(config.get("comet.game.rooms.maxPlayers"));

        roomPasswordEncryptionEnabled = Boolean.parseBoolean(config.get("comet.game.rooms.hashpasswords"));
        roomPasswordEncryptionRounds = Integer.parseInt(config.get("comet.game.rooms.hashrounds"));

        wordFilterMode = FilterMode.valueOf(config.get("comet.game.filter.mode").toUpperCase());
    }

    public static void setMotd(String motd) {
        messageOfTheDayEnabled = true;
        messageOfTheDayText = motd;
    }
}
