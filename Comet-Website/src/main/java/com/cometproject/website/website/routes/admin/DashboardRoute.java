package com.cometproject.website.website.routes.admin;

import com.cometproject.website.storage.dao.StatisticsDao;
import com.cometproject.website.website.WebsiteManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class DashboardRoute {
    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/admin/dashboard.vm");
    }

    public static String redirect(Request req, Response res) {
        res.redirect("/admin/dashboard");
        return "";
    }
}
