package com.cometproject.logger;

import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.logger.routes.Routes;
import com.cometproject.logger.tasks.CometThreadManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;


public class CometLogger {
    public static JSONTransformer JSON_TRANSFORMER = new JSONTransformer();

    private static CometThreadManagement threadManagement;

    private static final class JSONTransformer implements ResponseTransformer {
        private final Gson gson = new GsonBuilder().create();

        @Override
        public String render(Object model) throws Exception {
            if (model instanceof ResponseBuilder) { // in case you forget to call .get() i'll handle it for you ;)
                ResponseBuilder rb = (ResponseBuilder) model;
                return gson.toJson(rb.get());
            }

            return gson.toJson(model);
        }
    }

    public static void main(String[] args) {
        threadManagement = new CometThreadManagement();

        Routes.init();
    }

    public static CometThreadManagement getThreadManagement() {
        return threadManagement;
    }
}
