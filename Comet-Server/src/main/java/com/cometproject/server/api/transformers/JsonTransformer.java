package com.cometproject.server.api.transformers;

import com.cometproject.api.utilities.JsonUtil;
import spark.ResponseTransformer;


public class JsonTransformer implements ResponseTransformer {
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
            String gsonString = JsonUtil.getInstance().toJson(o);

            if (!gsonString.startsWith("{")) {
                return "{\"response\":" + gsonString + "}";
            } else {
                return gsonString;
            }
        } catch (Exception e) {
            return JsonUtil.getInstance().toJson(e);
        } finally {
            // Dispose object..
        }
    }
}
