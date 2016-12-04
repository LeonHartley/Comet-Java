package com.cometproject.website.website.routes;

import com.cometproject.website.config.Configuration;
import com.cometproject.website.players.Player;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.utilities.PasswordUtil;
import com.cometproject.website.website.WebsiteManager;
import org.mindrot.jbcrypt.BCrypt;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

public class IndexRoute {
    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        if(req.session().attribute("formError") != null) {
            model.put("formError", req.session().attribute("formError"));
            req.session().removeAttribute("formError");
        }

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/index.vm");
    }

    public static Route postLogin(Request req, Response res) {

        String username = req.queryParams("credentials.username");
        String password = req.queryParams("credentials.password");

        if(username.isEmpty() || password.isEmpty()) {
            req.session().attribute("formError", "empty");
            res.redirect("/");

            halt();
        }

        Player player = PlayerDao.getByCredentials(username, PasswordUtil.hash(password));

        if(player == null) {
            req.session().attribute("formError", "wrong");
            res.redirect("/");

            halt();
            return null;
        }

        // TODO: Check for ban
        req.session().attribute("player", player.getId());
        res.redirect("/me");
        return null;
    }

    public static ModelAndView logout(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        if(req.session().attribute("player") != null)
            req.session().attribute("player", null);

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/logout.vm");
    }
}
