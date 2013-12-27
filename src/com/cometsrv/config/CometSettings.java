package com.cometsrv.config;

import java.util.Properties;

public class CometSettings {
    public static boolean httpEnabled = false;
    public static String[] httpAllowedIPs = new String[] { };

    public static boolean messageOfTheDayEnabled = true;
    public static String messageOfTheDayText = "";

    public static boolean logChatToConsole = true;
    public static boolean logChatToMemory = true;
    public static boolean logChatToDatabase = true;

    public static String hotelName = "";
    public static String hotelUrl = "";
    public static String aboutImg = "";

    public static int groupCost = 0;

    public static boolean quartlyCreditsEnabled = false;
    public static int quartlyCreditsAmount = 0;

    public static void set(Properties properties) {
        httpEnabled = properties.getProperty("comet.network.http.enabled").equals("1");
        httpAllowedIPs = properties.getProperty("comet.network.http.allowed_ips").split(",");

        messageOfTheDayEnabled = properties.getProperty("comet.game.motd.enabled").equals("1");
        messageOfTheDayText = properties.getProperty("comet.game.motd.text");

        logChatToConsole = properties.getProperty("comet.game.chat.logToConsole").equals("1");
        logChatToConsole = properties.getProperty("comet.game.chat.logToMemory").equals("1");
        logChatToConsole = properties.getProperty("comet.game.chat.logToDatabase").equals("1");

        hotelName = properties.getProperty("comet.game.hotel.name");
        hotelUrl = properties.getProperty("comet.game.hotel.url");
        aboutImg = properties.getProperty("comet.game.about.img");

        groupCost = Integer.parseInt(properties.getProperty("comet.game.groups.cost"));

        quartlyCreditsEnabled = properties.getProperty("comet.game.quarterly.credits.enabled").equals("1");
        quartlyCreditsAmount = Integer.parseInt(properties.getProperty("comet.game.quarterly.credits.amount"));
    }

    public static void setMotd(String motd) {
        messageOfTheDayEnabled = true;
        messageOfTheDayText = motd;
    }
}
