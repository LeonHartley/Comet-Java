package com.cometproject.server.utilities;

import java.util.List;

public class BadgeUtil {
    private static String format(int num) {
        return (num < 10 ? "0" : "") + num;
    }

    public static String generate(int guildBase, int guildBaseColor, List<Integer> guildStates) {
        String badgeImage = "b" + format(guildBase) + "" + format(guildBaseColor);

        for(int i = 0; i < 3 * 4; i += 3) {
            badgeImage += i >= guildStates.size() ? "s" : "s" + format(guildStates.get(i)) + format(guildStates.get(i + 1)) + "" + guildStates.get(i + 2);
        }

        System.out.println(badgeImage);

        return badgeImage;
//
//        String str = "";
//        int num = 0;
//        String str2 = "b";
//        if (String.valueOf(guildBase).length() >= 2) {
//            str2 = str2 + guildBase;
//        } else {
//            str2 = str2 + "0" + guildBase;
//        }
//        str = String.valueOf(guildBaseColor);
//        if (str.length() >= 2) {
//            str2 = str2 + str;
//        } else if (str.length() <= 1) {
//            str2 = str2 + "0" + str;
//        }
//        int num2 = 0;
//        if (guildStates.get(9) != 0) {
//            num2 = 4;
//        } else if (guildStates.get(6) != 0) {
//            num2 = 3;
//        } else if (guildStates.get(3) != 0) {
//            num2 = 2;
//        } else if (guildStates.get(0) != 0) {
//            num2 = 1;
//        }
//        int num3 = 0;
//        for (int i = 0; i < num2; i++) {
//            str2 = str2 + "s";
//            num = guildStates.get(num3) - 20;
//            if (String.valueOf(num).length() >= 2) {
//                str2 = str2 + num;
//            } else {
//                str2 = str2 + "0" + num;
//            }
//            int num5 = guildStates.get(1 + num3);
//            str = String.valueOf(num5);
//            if (str.length() >= 2) {
//                str2 = str2 + str;
//            } else if (str.length() <= 1) {
//                str2 = str2 + "0" + str;
//            }
//            str2 = str2 + guildStates.get(2 + num3).toString();
//            switch (num3) {
//                case 0:
//                    num3 = 3;
//                    break;
//
//                case 3:
//                    num3 = 6;
//                    break;
//
//                case 6:
//                    num3 = 9;
//                    break;
//            }
//        }
//        return str2;
    }
}
