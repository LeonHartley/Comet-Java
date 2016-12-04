package com.cometproject.website.website;

import com.cometproject.website.config.Configuration;
import com.cometproject.website.players.Player;
import com.cometproject.website.settings.SiteSettings;
import com.cometproject.website.storage.dao.StatisticsDao;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.website.access.AccessLevel;
import com.cometproject.website.website.routes.*;
import com.cometproject.website.website.routes.admin.*;
import com.cometproject.website.website.routes.gamedata.TextsRoute;
import com.cometproject.website.website.routes.gamedata.VariablesRoute;
import com.sun.org.apache.xml.internal.resolver.Catalog;
import org.apache.velocity.app.VelocityEngine;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class WebsiteManager {
    private Map<String, AccessLevel> accessLevels = new HashMap<>();

    public WebsiteManager() {
        configure();
        registerAccessLevels();
        registerFilters();
        registerRoutes();
    }

    private void configure() {
        System.setProperty("java.net.preferIPv4Stack", "true");

        Spark.setPort(Configuration.getInstance().getSitePort());
    }

    private void registerAccessLevels() {
        accessLevels.put("/", AccessLevel.GUEST);
        accessLevels.put("/logout", AccessLevel.ALL);
        accessLevels.put("/registration/*", AccessLevel.GUEST);

        accessLevels.put("/me", AccessLevel.PLAYER);
        accessLevels.put("/account", AccessLevel.PLAYER);
        accessLevels.put("/account/", AccessLevel.PLAYER);
        accessLevels.put("/account/*", AccessLevel.PLAYER);
        accessLevels.put("/playlist", AccessLevel.PLAYER);
        accessLevels.put("/playlist/*", AccessLevel.PLAYER);

        accessLevels.put("/account/submit", AccessLevel.PLAYER);

        accessLevels.put("/client", AccessLevel.PLAYER);

        accessLevels.put("/community", AccessLevel.ALL);
        accessLevels.put("/articles", AccessLevel.ALL);
        accessLevels.put("/staff", AccessLevel.ALL);

        accessLevels.put("/admin/*", AccessLevel.ADMIN);

        accessLevels.put("/gamedata/*", AccessLevel.PLAYER);
    }

    private void registerRoutes() {
        VelocityTemplateEngine velocityTemplateEngine = new VelocityTemplateEngine(new VelocityEngine("./config/velocity.properties"));

        get("/", IndexRoute::index, velocityTemplateEngine);
        post("/", IndexRoute::postLogin);

        post("/registration/submit", RegistrationRoute::submit);

        get("/me", MeRoute::index, velocityTemplateEngine);

        get("/account", AccountRoute::index, velocityTemplateEngine);
        get("/account/", AccountRoute::index, velocityTemplateEngine);


        get("/account/:action", AccountRoute::index, velocityTemplateEngine);
        get("/account/disconnected", AccountRoute::disconnected, velocityTemplateEngine);

        get("/playlist", PlaylistRoute::index, velocityTemplateEngine);
        post("/playlist/add", PlaylistRoute::index, velocityTemplateEngine);
        get("/playlist/remove", PlaylistRoute::index, velocityTemplateEngine);

        post("/account/:action/submit", AccountRoute::submit);

        get("/client", ClientRoute::index, velocityTemplateEngine);
        get("/rapid", ClientRoute::rapid, velocityTemplateEngine);

        get("/community", CommunityRoute::index, velocityTemplateEngine);

        get("/articles", ArticlesRoute::index, velocityTemplateEngine);
        get("/articles/:id", ArticlesRoute::get, velocityTemplateEngine);

        get("/logout", IndexRoute::logout, velocityTemplateEngine);
        post("/logout", IndexRoute::logout, velocityTemplateEngine);

        get("/gamedata/external_variables", VariablesRoute::index);
        get("/gamedata/external_flash_texts", TextsRoute::index);
        get("/gamedata/external_texts", TextsRoute::index);

        // Admin routes
        get("/admin/", DashboardRoute::redirect);
        get("/admin", DashboardRoute::redirect);
        get("/housekeeping", DashboardRoute::redirect);
        get("/ase", DashboardRoute::redirect);
        get("/manage", DashboardRoute::redirect);

        get("/admin/dashboard", DashboardRoute::index, velocityTemplateEngine);
        get("/admin/rooms", RoomsRoute::index, velocityTemplateEngine);

        get("/admin/news", NewsRoute::index, velocityTemplateEngine);
        get("/admin/news/:id/edit", NewsRoute::edit, velocityTemplateEngine);
        post("/admin/news/:id/save", NewsRoute::save);
        post("/admin/news/create", NewsRoute::create);
        get("/admin/news/:id/delete", NewsRoute::delete);

        get("/admin/settings", SettingsRoute::index, velocityTemplateEngine);
        post("/admin/settings/save", SettingsRoute::save);

        get("/admin/cache", CacheRoute::index, velocityTemplateEngine);
        get("/admin/cache/reload", CacheRoute::reload);

        get("/admin/catalog", CatalogRoute::index, velocityTemplateEngine);
    }

    public void registerFilters() {
        accessLevels.forEach((k, v) -> before(k, (request, response) -> {
            // if playerRank = -1, player is not logged in.
            int playerRank = PlayerDao.getRankByPlayerId(request.session().attribute("player"));

            if (playerRank == -1 && (v != AccessLevel.ALL && v != AccessLevel.GUEST)) {
                response.redirect("/");
                halt();
            }

            if (v == AccessLevel.GUEST && playerRank != -1) {
                response.redirect("/me");
                halt();
            }

            if (v.getLevel() > playerRank) {
                if (playerRank != -1) {
                    response.redirect("/me");
                    halt();
                } else {
                    response.redirect("/");
                    halt();
                }
            }
        }));
    }

    public static Map<String, Object> applyGlobals(Map<String, Object> model) {
        model.put("authenticated", model.containsKey("player") && model.get("player") instanceof Player);
        model.put("config", Configuration.getInstance());
        model.put("siteSettings", SiteSettings.getInstance());
        model.put("serverStatistics", StatisticsDao.get());

        return model;
    }
}
