package com.cometproject.server.api.transformers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    /**
     * Create a reusable instance of GSON
     */
    private Gson gsonInstance = new Gson();

    /**
     * Render the template as JSON using the GSON instance
     *
     * @param o The object which we need to transform into JSON format
     * @return JSON formatted string
     * @throws Exception
     */
    @Override
    public String render(Object o) throws Exception {
        try {
            String gsonString = gsonInstance.toJson(o);

            if(!gsonString.startsWith("{")) {
                return "{\"response\":" + gsonString + "}";
            } else {
                return gsonString;
            }
        } catch (Exception e) {
            return gsonInstance.toJson(e);
        } finally {
            // Dispose object..
        }
    }
}
