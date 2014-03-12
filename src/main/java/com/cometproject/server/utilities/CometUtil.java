package com.cometproject.server.utilities;

public class CometUtil {
    public static boolean tryParseInteger(String s) {
        try {
            int product = Integer.parseInt(s);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
