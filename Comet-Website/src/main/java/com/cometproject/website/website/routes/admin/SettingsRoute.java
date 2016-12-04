package com.cometproject.website.website.routes.admin;

import com.cometproject.website.api.ApiClient;
import com.cometproject.website.settings.SiteSettings;
import com.cometproject.website.website.WebsiteManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class SettingsRoute {
    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        if(req.session().attribute("success") != null) {
            req.session().removeAttribute("success");

            model.put("success", true);
        }

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/admin/settings.vm");
    }

    public static Route save(Request req, Response res) {

        final String hotelName = req.queryParams("hotelName");
        final String hotelSlogan = req.queryParams("hotelSlogan");
        final String hotelDescription = req.queryParams("hotelDescription");

        final String twitterUsername = req.queryParams("twitterUsername");
        final String twitterWidgetId = req.queryParams("twitterWidget");
        final String facebookUsername = req.queryParams("facebookUsername");

        final int playerCredits = Integer.parseInt(req.queryParams("credits"));
        final int playerActivityPoints = Integer.parseInt(req.queryParams("activityPoints"));
        final int playerVipPoints = Integer.parseInt(req.queryParams("vipPoints"));
        final String playerFigure = req.queryParams("figure");
        final String playerMotto = req.queryParams("motto");
        final int playerHomeRoom = Integer.parseInt(req.queryParams("homeRoom"));

        final String gameIp = req.queryParams("gameIp");
        final int gamePort = Integer.parseInt(req.queryParams("gamePort"));
        final String gameClientBase = req.queryParams("gameClientBase");
        final String gameClientSwf = req.queryParams("gameClientSwf");
        final String gameClientVariables = req.queryParams("externalVariables");
        final String gameClientTexts = req.queryParams("externalTexts");
        final String gameClientFurnidata = req.queryParams("furniData");
        final String gameClientProductdata = req.queryParams("productData");

        // TODO: Null check

        SiteSettings.getInstance().setHotelName(hotelName);
        SiteSettings.getInstance().setHotelSlogan(hotelSlogan);
        SiteSettings.getInstance().setHotelDescription(hotelDescription);

        SiteSettings.getInstance().setTwitterUsername(twitterUsername);
        SiteSettings.getInstance().setTwitterWidgetId(twitterWidgetId);
        SiteSettings.getInstance().setFacebookUsername(facebookUsername);

        SiteSettings.getInstance().setPlayerDefaultCredits(playerCredits);
        SiteSettings.getInstance().setPlayerDefaultActivityPoints(playerActivityPoints);
        SiteSettings.getInstance().setPlayerDefaultVipPoints(playerVipPoints);
        SiteSettings.getInstance().setPlayerDefaultFigure(playerFigure);
        SiteSettings.getInstance().setPlayerDefaultMotto(playerMotto);
        SiteSettings.getInstance().setPlayerDefaultHomeRoom(playerHomeRoom);

        SiteSettings.getInstance().setGameHost(gameIp);
        SiteSettings.getInstance().setGamePort(gamePort);
        SiteSettings.getInstance().setGameClientBase(gameClientBase);
        SiteSettings.getInstance().setGameClientSwf(gameClientSwf);
        SiteSettings.getInstance().setGameClientVariables(gameClientVariables);
        SiteSettings.getInstance().setGameClientTexts(gameClientTexts);
        SiteSettings.getInstance().setGameClientFurniData(gameClientFurnidata);
        SiteSettings.getInstance().setGameClientProductData(gameClientProductdata);

        SiteSettings.getInstance().save();

        req.session().attribute("success", true);
        res.redirect("/admin/settings");
        return null;
    }
}
