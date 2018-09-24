package com.cometproject.server.storage.queries.player;

import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.game.players.data.components.navigator.ISavedSearch;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.components.types.navigator.SavedSearch;
import com.cometproject.server.game.players.data.PlayerAvatarData;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.storage.SqlHelper;
import com.cometproject.server.storage.cache.CacheManager;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerDao {
    public static Player getPlayer(String ssoTicket) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT p.id as playerId, p.username AS playerData_username, p.figure AS playerData_figure, p.motto AS playerData_motto, p.credits AS playerData_credits, p.vip_points AS playerData_vipPoints, p.rank AS playerData_rank, p.last_ip AS playerData_lastIp, p.seasonal_points AS playerData_seasonalPoints, " +
                    " p.vip AS playerData_vip, p.gender AS playerData_gender, p.last_online AS playerData_lastOnline, p.reg_timestamp AS playerData_regTimestamp, p.reg_date AS playerData_regDate, p.favourite_group AS playerData_favouriteGroup, p.achievement_points AS playerData_achievementPoints," +
                    " p.email AS playerData_email, p.activity_points AS playerData_activityPoints, p.quest_id AS playerData_questId, p.time_muted AS playerData_timeMuted, p.name_colour AS playerData_nameColour, \n" +
                    "  pSettings.volume AS playerSettings_volume, pSettings.home_room AS playerSettings_homeRoom, pSettings.hide_online AS playerSettings_hideOnline, pSettings.hide_inroom AS playerSettings_hideInRoom, pSettings.ignore_invites AS playerSettings_ignoreInvites, \n" +
                    "   pSettings.allow_friend_requests AS playerSettings_allowFriendRequests, pSettings.allow_trade AS playerSettings_allowTrade, pSettings.allow_follow AS playerSettings_allowFollow, pSettings.allow_mimic AS playerSettings_allowMimic, pSettings.wardrobe AS playerSettings_wardrobe, pSettings.playlist AS playerSettings_playlist, pSettings.chat_oldstyle AS playerSettings_useOldChat,\n" +
                    " pSettings.navigator_x AS playerSettings_navigatorX, pSettings.navigator_y AS playerSettings_navigatorY, pSettings.navigator_height AS playerSettings_navigatorHeight, pSettings.navigator_width AS playerSettings_navigatorWidth, pSettings.navigator_show_searches AS playerSettings_navigatorShowSearches, pSettings.ignore_events AS playerSettings_ignoreEvents, pSettings.disable_whisper AS playerSettings_disableWhisper, pSettings.send_login_notif AS playerSettings_sendLoginNotif, pSettings.personalstaff AS playerSettings_personalstaff, " +
                    "  pStats.achievement_score AS playerStats_achievementPoints, pStats.daily_respects AS playerStats_dailyRespects, pStats.total_respect_points AS playerStats_totalRespectPoints, pStats.help_tickets AS playerStats_helpTickets, pStats.help_tickets_abusive AS playerStats_helpTicketsAbusive, pStats.cautions AS playerStats_cautions, pStats.bans AS playerStats_bans, pStats.daily_scratches AS playerStats_scratches \n" +
                    "FROM players p\n" +
                    " JOIN player_settings pSettings ON pSettings.player_id = p.id \n" +
                    " JOIN player_stats pStats ON pStats.player_id = p.id\n" +
                    "\n" +
                    "WHERE p.auth_ticket = ?", sqlConnection);
            preparedStatement.setString(1, ssoTicket);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new Player(resultSet, false);
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return null;
    }

    public static Player getPlayerFallback(String ssoTicket) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT p.id as playerId, p.username AS playerData_username, p.figure AS playerData_figure, p.motto AS playerData_motto, p.credits AS playerData_credits, p.vip_points AS playerData_vipPoints, p.rank AS playerData_rank, p.vip AS playerData_vip, p.gender AS playerData_gender, p.last_online AS playerData_lastOnline, p.reg_timestamp AS playerData_regTimestamp, p.reg_date AS playerData_regDate, p.favourite_group AS playerData_favouriteGroup, p.achievement_points AS playerData_achievementPoints, p.email AS playerData_email, p.activity_points AS playerData_activityPoints, p.seasonal_points AS playerData_seasonalPoints, p.quest_id AS playerData_questId, p.last_ip AS playerData_lastIp, p.time_muted AS playerData_timeMuted, p.name_colour AS playerData_nameColour \n" +
                    "FROM players p " +
                    "WHERE p.auth_ticket = ?", sqlConnection);
            preparedStatement.setString(1, ssoTicket);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new Player(resultSet, true);
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return null;
    }

    public static PlayerData getDataByUsername(String username) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT id, motto, figure, gender, email, rank, credits, vip_points, activity_points, seasonal_points, reg_date, last_online, vip, achievement_points, reg_timestamp, favourite_group, last_ip, quest_id, time_muted, name_colour FROM players WHERE username = ?", sqlConnection);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new PlayerData(resultSet.getInt("id"), username, resultSet.getString("motto"), resultSet.getString("figure"), resultSet.getString("gender"),
                        resultSet.getString("email") == null ? "" : resultSet.getString("email"), resultSet.getInt("rank"), resultSet.getInt("credits"), resultSet.getInt("vip_points"),
                        resultSet.getInt("activity_points"), resultSet.getInt("seasonal_points"), resultSet.getString("reg_date"), resultSet.getInt("last_online"), resultSet.getString("vip").equals("1"),
                        resultSet.getInt("achievement_points"), resultSet.getInt("reg_timestamp"), resultSet.getInt("favourite_group"), resultSet.getString("last_ip"), resultSet.getInt("quest_id"), resultSet.getInt("time_muted"), resultSet.getString("name_colour"), null);
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return null;
    }

    public static PlayerData getDataById(int id) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT id, username, motto, figure, gender, email, rank, credits, vip_points, activity_points, seasonal_points, reg_date, last_online, vip, name_colour, achievement_points, reg_timestamp, favourite_group, last_ip, quest_id, time_muted, name_colour FROM players WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new PlayerData(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("motto"), resultSet.getString("figure"), resultSet.getString("gender"),
                        resultSet.getString("email") == null ? "" : resultSet.getString("email"), resultSet.getInt("rank"), resultSet.getInt("credits"), resultSet.getInt("vip_points"),
                        resultSet.getInt("activity_points"), resultSet.getInt("seasonal_points"), resultSet.getString("reg_date"), resultSet.getInt("last_online"), resultSet.getString("vip").equals("1"),
                        resultSet.getInt("achievement_points"), resultSet.getInt("reg_timestamp"), resultSet.getInt("favourite_group"), resultSet.getString("last_ip"), resultSet.getInt("quest_id"), resultSet.getInt("time_muted"), resultSet.getString("name_colour"), null);
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return null;
    }

    public static PlayerAvatar getAvatarById(int id, byte mode) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            String query;

            switch (mode) {
                case PlayerAvatar.USERNAME_FIGURE:
                    query = "SELECT username, figure, gender, motto FROM players WHERE id = ?";
                    break;


                default:
                case PlayerAvatar.USERNAME_FIGURE_MOTTO:
                    query = "SELECT username, figure, gender, motto FROM players WHERE id = ?";
                    break;
            }

            preparedStatement = SqlHelper.prepare(query, sqlConnection);

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final PlayerAvatar playerAvatar = new PlayerAvatarData(id, resultSet.getString("username"), resultSet.getString("figure"), resultSet.getString("gender"), resultSet.getString("motto"));

                if (mode == PlayerAvatar.USERNAME_FIGURE_MOTTO) {
                    playerAvatar.setMotto(resultSet.getString("motto"));
                }

                return playerAvatar;
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return null;
    }

    public static String getMottoByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            String query = "SELECT motto FROM players WHERE id = ?";

            preparedStatement = SqlHelper.prepare(query, sqlConnection);

            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getString("motto");
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return "";
    }

    public static PlayerSettings getSettingsById(int id) {
        // TODO: Cache, cache, cache!

        Connection sqlConnection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM player_settings WHERE player_id = ? LIMIT 1;", sqlConnection);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new PlayerSettings(resultSet, false, null);
            } else {
                // close old statement
                SqlHelper.closeSilently(preparedStatement);

                preparedStatement = SqlHelper.prepare("INSERT into player_settings (`player_id`) VALUES(?)", sqlConnection);
                preparedStatement.setInt(1, id);

                SqlHelper.executeStatementSilently(preparedStatement, false);

                return new PlayerSettings();
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return new PlayerSettings();
    }

    public static PlayerStatistics getStatisticsById(int id) {
        if (id < 0) return null; // todo: figure out why this would be negative

        Connection sqlConnection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM player_stats WHERE player_id = ? LIMIT 1;", sqlConnection);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new PlayerStatistics(resultSet, false, null);
            } else {
                SqlHelper.closeSilently(preparedStatement);

                preparedStatement = SqlHelper.prepare("INSERT into player_stats (`player_id`) VALUES(?)", sqlConnection);
                preparedStatement.setInt(1, id);

                SqlHelper.executeStatementSilently(preparedStatement, false);

                return new PlayerStatistics(id);
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return new PlayerStatistics(id);
    }

    public static void updatePlayerStatus(Player player, boolean online, boolean setLastOnline) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE players SET online = ?" + (setLastOnline ? ", last_online = ?, last_ip = ?" : "") + " WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, online ? "1" : "0");

            if (setLastOnline) {
                preparedStatement.setLong(2, Comet.getTime());
                preparedStatement.setString(3, player.getData().getIpAddress());
                preparedStatement.setInt(4, player.getId());
            } else {
                preparedStatement.setInt(2, player.getId());
            }

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static String getUsernameByPlayerId(int playerId) {
        if (CacheManager.getInstance().isEnabled()) {
            if (CacheManager.getInstance().exists("players.username." + playerId)) {
                return CacheManager.getInstance().getString("players.username." + playerId);
            }
        }

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `username` FROM players WHERE `id` = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final String username = resultSet.getString("username");

                if (CacheManager.getInstance().isEnabled()) {
                    CacheManager.getInstance().putString("players.username." + playerId, username);
                }

                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return null;
    }

    public static int getIdByUsername(String username) {
        if (PlayerManager.getInstance().getPlayerIdByUsername(username) != -1)
            return PlayerManager.getInstance().getPlayerIdByUsername(username);

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `id` FROM players WHERE `username` = ?", sqlConnection);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return 0;
    }

    public static String getIpAddress(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `last_ip` FROM players WHERE `id` = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getString("last_ip");
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return "";
    }

    public static void updatePlayerData(int id, String username, String motto, String figure, int credits, int points, String gender, int favouriteGroup, int activityPoints, int seasonalPoints, int questId, int achievementPoints, String nameColour) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE players SET username = ?, motto = ?, figure = ?, credits = ?, vip_points = ?, gender = ?, favourite_group = ?, activity_points = ?, seasonal_points = ?, quest_id = ?, achievement_points = ?, name_colour = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, motto);
            preparedStatement.setString(3, figure);
            preparedStatement.setInt(4, credits);
            preparedStatement.setInt(5, points);
            preparedStatement.setString(6, gender);
            preparedStatement.setInt(7, favouriteGroup);
            preparedStatement.setInt(8, activityPoints);
            preparedStatement.setInt(9, seasonalPoints);
            preparedStatement.setInt(10, questId);
            preparedStatement.setInt(11, achievementPoints);
            preparedStatement.setString(12, nameColour);
            preparedStatement.setInt(13, id);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static boolean updatePlayerStatistics(PlayerStatistics playerStatistics) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_stats SET achievement_score = ?, total_respect_points = ?, daily_respects = ?, help_tickets = ?, help_tickets_abusive = ?, cautions = ?, bans = ?, daily_scratches = ? WHERE player_id = ?", sqlConnection);
            preparedStatement.setInt(1, playerStatistics.getAchievementPoints());
            preparedStatement.setInt(2, playerStatistics.getRespectPoints());
            preparedStatement.setInt(3, playerStatistics.getDailyRespects());
            preparedStatement.setInt(4, playerStatistics.getHelpTickets());
            preparedStatement.setInt(5, playerStatistics.getAbusiveHelpTickets());
            preparedStatement.setInt(6, playerStatistics.getCautions());
            preparedStatement.setInt(7, playerStatistics.getBans());
            preparedStatement.setInt(8, playerStatistics.getScratches());

            preparedStatement.setInt(9, playerStatistics.getPlayerId());

            return preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return false;
    }

    public static void updateHomeRoom(int homeRoom, int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_settings SET home_room = ? WHERE player_id = ?", sqlConnection);
            preparedStatement.setInt(1, homeRoom);
            preparedStatement.setInt(2, userId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveWardrobe(String wardrobeData, int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_settings SET wardrobe = ? WHERE player_id = ?", sqlConnection);
            preparedStatement.setString(1, wardrobeData);
            preparedStatement.setInt(2, userId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveVolume(String volumeData, int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_settings SET volume = ? WHERE player_id = ?", sqlConnection);
            preparedStatement.setString(1, volumeData);
            preparedStatement.setInt(2, userId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveChatStyle(boolean useOldChat, int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_settings SET chat_oldstyle = ? WHERE player_id = ?", sqlConnection);
            preparedStatement.setString(1, useOldChat ? "1" : "0");
            preparedStatement.setInt(2, userId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveIgnoreInvitations(boolean ignoreInvitations, int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_settings SET ignore_invites = ? WHERE player_id = ?", sqlConnection);
            preparedStatement.setString(1, ignoreInvitations ? "1" : "0");
            preparedStatement.setInt(2, userId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveAllowFriendRequests(boolean allowFriendRequests, int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_settings SET allow_friend_requests = ? WHERE player_id = ?", sqlConnection);
            preparedStatement.setString(1, allowFriendRequests ? "1" : "0");
            preparedStatement.setInt(2, userId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveIgnoreEvents(boolean ignoreEvents, int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_settings SET ignore_events = ? WHERE player_id = ?", sqlConnection);
            preparedStatement.setString(1, ignoreEvents ? "1" : "0");
            preparedStatement.setInt(2, userId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void savePersonalStaff(boolean personalStaff, int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_settings SET personalstaff = ? WHERE player_id = ?", sqlConnection);
            preparedStatement.setString(1, personalStaff ? "1" : "0");
            preparedStatement.setInt(2, userId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static boolean usernameIsAvailable(String username) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `id` FROM players WHERE username = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return false;
    }

    public static void resetHomeRoom(int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_settings SET home_room = 0 WHERE home_room = ?", sqlConnection);
            preparedStatement.setInt(1, roomId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void resetOnlineStatus() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE players SET online = '0' WHERE online = '1'", sqlConnection);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void nullifyAuthTicket(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE players SET auth_ticket = NULL WHERE id = ?", sqlConnection);

            preparedStatement.setInt(1, playerId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }


    public static void saveBatch(Map<Integer, PlayerData> playerData) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE players SET username = ?, motto = ?, figure = ?, credits = ?, vip_points = ?, gender = ?, favourite_group = ?, activity_points = ?, quest_id = ?, achievement_points = ? WHERE id = ?;", sqlConnection);

            for (PlayerData playerDataInstance : playerData.values()) {
                preparedStatement.setString(1, playerDataInstance.getUsername());
                preparedStatement.setString(2, playerDataInstance.getMotto());
                preparedStatement.setString(3, playerDataInstance.getFigure());
                preparedStatement.setInt(4, playerDataInstance.getCredits());
                preparedStatement.setInt(5, playerDataInstance.getVipPoints());
                preparedStatement.setString(6, playerDataInstance.getGender());
                preparedStatement.setInt(7, playerDataInstance.getFavouriteGroup());
                preparedStatement.setInt(8, playerDataInstance.getActivityPoints());
                preparedStatement.setInt(9, playerDataInstance.getQuestId());
                preparedStatement.setInt(10, playerDataInstance.getAchievementPoints());
                preparedStatement.setInt(11, playerDataInstance.getId());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void dailyPlayerUpdate(int dailyRespects, int dailyScratches) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_stats SET daily_respects = ?, daily_scratches = ?;", sqlConnection);

            preparedStatement.setInt(1, dailyRespects);
            preparedStatement.setInt(2, dailyScratches);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void addTimeMute(int playerId, int timeMuted) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("UPDATE players SET time_muted = ? WHERE id = ?");
            preparedStatement.setInt(1, timeMuted);
            preparedStatement.setInt(2, playerId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void updateDisableWhisper(final boolean disableWhisper, int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("UPDATE players SET disable_whisper = ? WHERE player_id = ?");
            preparedStatement.setString(1, disableWhisper ? "1" : "0");
            preparedStatement.setInt(2, playerId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static int getUsernameAlreadyExist(String username) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT COUNT(0) as exist FROM players WHERE username = ?", sqlConnection);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt("exist");
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return 1;
    }

    public static void updatePlayersUsername(String newName, int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE players SET username = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, playerId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void updateRoomsUsername(String newName, int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE rooms SET owner = ? WHERE owner_id = ?", sqlConnection);
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, playerId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveNameChangeLog(int playerId, String newName, String oldName) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("INSERT INTO logs_namechange (`user_id`, `new_name`, `old_name`, `timestamp`) VALUES(?, ?, ?, ?)", sqlConnection);

            preparedStatement.setInt(1, playerId);
            preparedStatement.setString(2, newName);
            preparedStatement.setString(3, oldName);
            preparedStatement.setInt(4, (int) Comet.getTime());

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveNavigatorSettings(int navigatorX, int navigatorY, int navigatorHeight, int navigatorWidth, boolean navigatorShowSearches, int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_settings SET navigator_x = ?, navigator_y = ?, navigator_height = ?, navigator_width = ?, navigator_show_searches = ? WHERE player_id = ?", sqlConnection);

            preparedStatement.setInt(1, navigatorX);
            preparedStatement.setInt(2, navigatorY);
            preparedStatement.setInt(3, navigatorHeight);
            preparedStatement.setInt(4, navigatorWidth);
            preparedStatement.setString(5, navigatorShowSearches ? "1" : "0");
            preparedStatement.setInt(6, playerId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static Set<Integer> getFavouriteRooms(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final Set<Integer> data = new ConcurrentHashSet<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `room_id` FROM `player_favourite_rooms` WHERE `player_id` = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(resultSet.getInt("room_id"));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static void saveFavouriteRoom(int playerId, int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into player_favourite_rooms (player_id, room_id) VALUES(?, ?);", sqlConnection);

            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, roomId);
            preparedStatement.execute();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void deleteFavouriteRoom(int playerId, int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("DELETE FROM player_favourite_rooms WHERE player_id = ? AND room_id = ?", sqlConnection);

            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, roomId);
            preparedStatement.execute();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static Map<Integer, ISavedSearch> getSavedSearches(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final Map<Integer, ISavedSearch> data = new ConcurrentHashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `id`, `view`, `search_query` FROM player_saved_searches WHERE player_id = ? LIMIT 50;", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getInt("id"), new SavedSearch(resultSet.getString("view"), resultSet.getString("search_query")));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static Map<String, Integer> getViewModes(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final Map<String, Integer> data = new ConcurrentHashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM player_navigator_view_modes WHERE player_id = ? LIMIT 50;", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getString("category"), resultSet.getInt("view_mode"));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static void saveViewMode(String category, int viewMode, int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("REPLACE into player_navigator_view_modes (player_id, category, view_mode) VALUES(?, ?, ?);", sqlConnection);
            preparedStatement.setInt(1, playerId);
            preparedStatement.setString(2, category);
            preparedStatement.setInt(3, viewMode);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static int saveSearch(int playerId, SavedSearch savedSearch) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into player_saved_searches (player_id, view, search_query) VALUES(?, ?, ?);", sqlConnection, true);

            preparedStatement.setInt(1, playerId);
            preparedStatement.setString(2, savedSearch.getView());
            preparedStatement.setString(3, savedSearch.getSearchQuery());

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return 0;
    }

    public static void deleteSearch(int searchId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("DELETE FROM player_saved_searches WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, searchId);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static Set<Integer> getEffects(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final Set<Integer> data = new ConcurrentHashSet<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `effect_id` FROM `player_effects` WHERE `player_id` = ?;", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(resultSet.getInt("effect_id"));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static void saveEffect(int playerId, int effectId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into player_effects (player_id, effect_id) VALUES(?,?);", sqlConnection);
            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, effectId);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

}