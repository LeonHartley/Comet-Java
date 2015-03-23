package com.cometproject.server.utilities;

import com.cometproject.server.config.CometSettings;

import java.util.Map;

public class FilterUtil {
    public static String normalize(String string) {
        String result = string;

        for(Map.Entry<String, String> chars : CometSettings.strictFilterCharacters.entrySet()) {
            result = result.replace(chars.getKey(), chars.getValue());
        }

        return result;
    }
}
