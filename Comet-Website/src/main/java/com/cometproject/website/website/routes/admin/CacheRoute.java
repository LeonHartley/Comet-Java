package com.cometproject.website.website.routes.admin;

import com.cometproject.website.gamedata.GameDataCache;
import com.cometproject.website.website.WebsiteManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class CacheRoute {
    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        if(req.session().attribute("success") != null) {
            req.session().removeAttribute("success");

            model.put("success", true);
        }

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/admin/cache.vm");
    }

    public static String reload(Request req, Response res) {
        GameDataCache.getInstance().reload();

        req.session().attribute("success", true);
        res.redirect("/admin/cache");

        return null;
    }
}
