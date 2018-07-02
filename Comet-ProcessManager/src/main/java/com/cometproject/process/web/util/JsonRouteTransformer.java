package com.cometproject.process.web.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonRouteTransformer implements ResponseTransformer {
    private static final JsonRouteTransformer jsonTransformer = new JsonRouteTransformer();

    private final Gson gson = new Gson();

    @Override
    public String render(Object o) {
        return gson.toJson(o);
    }

    public static JsonRouteTransformer getInstance() {
        return jsonTransformer;
    }
}
