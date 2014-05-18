package com.cometproject.server.game.players.data;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class PlayerLoader {
    //private static Logger log = Logger.getLogger(PlayerLoader.class.getName());

    public static Player loadPlayerBySSo(String ticket) {
        int id = PlayerDao.getIdBySSO(ticket);

        if(id == 0) return null;

        return new Player(id);
    }

    public static Player loadPlayerByIdAndTicket(int id, String ticket) {
        String databaseTicket = PlayerDao.getAuthTicketById(id);

        if(!databaseTicket.equals(ticket)) {
            return null;
        }

        return new Player(id);
    }

    public static PlayerSettings loadSettings(int id) {
        return PlayerDao.getSettingsById(id);
    }

    public static PlayerStatistics loadStatistics(int id) {
        return PlayerDao.getStatisticsById(id);
    }

    public static PlayerData loadDataById(int id) {
        return PlayerDao.getDataById(id);
    }
}