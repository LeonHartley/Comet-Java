package com.cometproject.server.config;

import java.util.Properties;

public class CometSettings {
    public static boolean httpEnabled = false;

    public static boolean messageOfTheDayEnabled = true;
    public static String messageOfTheDayText = "";

    @Deprecated
    public static boolean logChatToConsole = true;

    @Deprecated
    public static boolean logChatToMemory = true;

    @Deprecated
    public static boolean logChatToDatabase = true;

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

    public static void set(Properties properties) {
        httpEnabled = properties.getProperty("comet.network.http.enabled").equals("1");

        messageOfTheDayEnabled = properties.getProperty("comet.game.motd.enabled").equals("1");
        messageOfTheDayText = properties.getProperty("comet.game.motd.text");

        logChatToConsole = properties.getProperty("comet.game.chat.logToConsole").equals("1");
        logChatToMemory = properties.getProperty("comet.game.chat.logToMemory").equals("1");
        logChatToDatabase = properties.getProperty("comet.game.chat.logToDatabase").equals("1");

        logStore = properties.getProperty("comet.game.logging.store");

        hotelName = properties.getProperty("comet.game.hotel.name");
        hotelUrl = properties.getProperty("comet.game.hotel.url");
        aboutImg = properties.getProperty("comet.game.about.img");

        groupCost = Integer.parseInt(properties.getProperty("comet.game.groups.cost"));

        quartlyCreditsEnabled = properties.getProperty("comet.game.quarterly.credits.enabled").equals("1");
        quartlyCreditsAmount = Integer.parseInt(properties.getProperty("comet.game.quarterly.credits.amount"));

        showUptimeInAbout = properties.getProperty("comet.about.command.uptime").equals("1");
        showActiveRoomsInAbout = properties.getProperty("comet.about.command.activeRooms").equals("1");
        showUsersOnlineInAbout = properties.getProperty("comet.about.command.usersOnline").equals("1");

        floorMaxX = Integer.parseInt(properties.getProperty("comet.floor.command.max.x"));
        floorMaxY = Integer.parseInt(properties.getProperty("comet.floor.command.max.y"));
        floorMaxTotal = Integer.parseInt(properties.getProperty("comet.floor.command.max.total"));

        try {
            maxPlayersInRoom = Integer.parseInt(properties.getProperty("comet.game.rooms.maxPlayers"));
        } catch(Exception e) {
            // fall back to 150
        }
    }

    public static void setMotd(String motd) {
        messageOfTheDayEnabled = true;
        messageOfTheDayText = motd;
    }
}
