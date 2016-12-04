package com.cometproject.website.website.routes.admin;


import com.cometproject.website.api.ApiClient;
import com.cometproject.website.website.WebsiteManager;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class RoomsRoute {
    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        model.put("rooms", ApiClient.getInstance().execute("/rooms/active/all").toString());

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/admin/rooms.vm");
    }

//    public static ModelAndView alert(Request req, Response res) {
//        Map<String, Object> model = new HashMap<>();
//
//        if(req.)
//
//        final JSONObject object = ApiClient.getInstance().execute("")
//
//        model.put("success", ApiClient.getInstance().execute(""))
//    }

}
