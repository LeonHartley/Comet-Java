package com.cometproject.website.website.routes.admin;

import com.cometproject.website.website.WebsiteManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class CatalogRoute {
    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/admin/catalog.vm");
    }
}
