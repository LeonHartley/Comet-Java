package com.cometproject.server.game.players.data;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.storage.queries.player.PlayerDao;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerLoader {
    private static Logger log = Logger.getLogger(PlayerLoader.class.getName());

    public static Player loadPlayerBySSo(String ticket) {
        return new Player(PlayerDao.getIdBySSO(ticket));
    }

    public static PlayerSettings loadSettings(int id) {
        try {
            ResultSet result = Comet.getServer().getStorage().getRow("SELECT * FROM player_settings WHERE player_id = " + id);
            return new PlayerSettings(result);
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                Comet.getServer().getStorage().execute("INSERT into player_settings (`player_id`) VALUES(" + id + ")");
            } else {
                log.error("Error while loading player settings", e);
            }
        }

        return new PlayerSettings();
    }

    public static PlayerStatistics loadStatistics(int id) {
        try {
            ResultSet result = Comet.getServer().getStorage().getRow("SELECT * FROM player_stats WHERE player_id = " + id);
            return new PlayerStatistics(result);
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                Comet.getServer().getStorage().execute("INSERT into player_stats (`player_id`) VALUES(" + id + ")");
            } else {
                log.error("Error while loading player statistics", e);
            }
        }

        return new PlayerStatistics(id);
    }


    public static Player resultToPlayer(ResultSet result) throws SQLException {
        if (result == null) {
            return null;
        }

        if (!result.next()) {
            return null;
        }

        return new Player(result.getInt("id"));
    }

    public static PlayerData loadDataById(int id) {
        return PlayerDao.getDataById(id);
    }
}