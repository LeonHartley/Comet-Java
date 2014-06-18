package com.cometproject.server.api.transformers;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    /**
     * Create a reusable instance of GSON
     */
    private Gson gsonInstance = new Gson();

    /**
     * Render the template as JSON using the GSON instance
     * @param o The object which we need to transform into JSON format
     * @return JSON formatted string
     * @throws Exception
     */
    @Override
    public String render(Object o) throws Exception {
        return gsonInstance.toJson(o);
    }
}
