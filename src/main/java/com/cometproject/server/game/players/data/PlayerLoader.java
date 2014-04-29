package com.cometproject.server.game.players.data;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class PlayerLoader {
    public static Player loadPlayerBySSo(String ticket) {
        return new Player(PlayerDao.getIdBySSO(ticket));
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