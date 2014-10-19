package com.cometproject.server.api.routes;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import javolution.util.FastMap;
import org.apache.commons.lang.StringUtils;
import spark.Request;
import spark.Response;

import java.util.Map;

public class PlayerRoutes {
    public static Object reloadPlayerData(Request request, Response response) {
        Map<String, Object> result = new FastMap<>();
        response.type("application/json");

        if(!StringUtils.isNumeric(request.params("id"))) {
            result.put("error", "Invalid ID");
            return result;
        }

        int playerId = Integer.parseInt(request.params("id"));

        if(!CometManager.getPlayers().isOnline(playerId)) {
            result.put("error", "Player is not online");
            return result;
        }

        Session session = Comet.getServer().getNetwork().getSessions().getByPlayerId(playerId);

        if(session == null) {
            result.put("error", "Unable to find the player's session");
            return result;
        }

        PlayerData newPlayerData = PlayerDao.getDataById(playerId);
        PlayerData currentPlayerData = session.getPlayer().getData();

        final boolean sendCurrencies = (newPlayerData.getCredits() != currentPlayerData.getCredits()) ||
                (newPlayerData.getActivityPoints() != currentPlayerData.getActivityPoints()) ||
                (newPlayerData.getVipPoints() != currentPlayerData.getVipPoints());

        currentPlayerData.setRank(newPlayerData.getRank());
        currentPlayerData.setMotto(newPlayerData.getMotto());
        currentPlayerData.setFigure(newPlayerData.getFigure());
        currentPlayerData.setGender(newPlayerData.getGender());
        currentPlayerData.setEmail(newPlayerData.getEmail());
        currentPlayerData.setCredits(newPlayerData.getCredits());
        currentPlayerData.setVipPoints(newPlayerData.getVipPoints());
        currentPlayerData.setActivityPoints(newPlayerData.getActivityPoints());
        currentPlayerData.setFavouriteGroup(newPlayerData.getFavouriteGroup());
        currentPlayerData.setVip(newPlayerData.isVip());

        if(sendCurrencies) {
            session.getPlayer().sendBalance();
        }

        session.getPlayer().poof();

        result.put("success", true);
        return result;
    }
}
