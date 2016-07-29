package com.cometproject.server.utilities;

import com.google.gson.Gson;


public class JsonUtil {
    private static final Gson gson = new Gson();

    public static Gson getInstance() {
        return gson;
    }
}
