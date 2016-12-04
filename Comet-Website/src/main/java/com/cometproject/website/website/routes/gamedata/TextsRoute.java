package com.cometproject.website.website.routes.gamedata;

import com.cometproject.website.gamedata.GameDataCache;
import spark.Request;
import spark.Response;

public class TextsRoute {
    public static String index(Request req, Response res) {
        res.type("text/plain");
        return GameDataCache.getInstance().getCachedTexts();
    }
}
