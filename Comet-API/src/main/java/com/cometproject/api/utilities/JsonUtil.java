package com.cometproject.api.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JsonUtil {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static Gson getInstance() {
        return gson;
    }
}
