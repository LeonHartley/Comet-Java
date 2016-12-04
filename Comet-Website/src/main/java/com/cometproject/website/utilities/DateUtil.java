package com.cometproject.website.utilities;

import java.text.DateFormat;

public class DateUtil {
    private static DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

    public static String format(int unixTimestamp) {
        return formatter.format(new java.util.Date(unixTimestamp * 1000L));
    }
}
