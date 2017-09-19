package com.cometproject.website.storage.dao.players;

import com.cometproject.website.boot.CometWebsite;
import com.cometproject.website.players.Player;
import com.cometproject.website.players.PlayerPreferences;
import com.cometproject.website.players.items.PlayerItem;
import com.cometproject.website.settings.SiteSettings;
import com.cometproject.website.storage.dao.DaoHelper;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PlayerDao {
    public static Player getByCredentials(String username, String password) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT * FROM players WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new Player(resultSet);
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return null;
    }

    public static Player getById(int id) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT * FROM players WHERE id = ?");
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new Player(resultSet);
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return null;
    }

    public static List<Player> searchByUsername(String username) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final List<Player> data = new LinkedList<>();

        try {
            sqlConnection = DaoHelper.getConnection();

            username = username + "%";

            preparedStatement = sqlConnection.prepareStatement("SELECT * FROM players WHERE username LIKE ?");
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(new Player(resultSet));
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return data;
    }

    public static List<PlayerItem> getInventory(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final List<PlayerItem> data = new LinkedList<>();

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement(
                    "SELECT i.id, f.item_name, count(`item_name`) as amount FROM items AS i RIGHT JOIN furniture f ON f.id = i.base_item WHERE i.user_id = ? AND i.room_id = 0 GROUP BY `item_name`"
            );

            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final PlayerItem playerItem = new PlayerItem();

                playerItem.id = resultSet.getInt("id");
                playerItem.itemName = resultSet.getString("item_name");
                playerItem.amount = resultSet.getInt("amount");

                data.add(playerItem);
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return data;
    }

    public static int getRankByPlayerId(Integer id) {
        if (id == null)
            return -1;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT rank FROM players WHERE id = ?");
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return -1;
    }

    public static boolean isEmailAvailable(String email) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT 0 FROM players WHERE email = ?");
            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return true;
    }

    public static boolean isUsernameAvailable(String username) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT 0 FROM players WHERE username = ?");
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return true;
    }

    public static Player create(String username, String email, String password) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement(
                    "INSERT into players (username, email, password, figure, motto, credits, activity_points, vip_points, reg_date, reg_timestamp, last_online) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, SiteSettings.getInstance().getPlayerDefaultFigure());
            preparedStatement.setString(5, SiteSettings.getInstance().getPlayerDefaultMotto());
            preparedStatement.setInt(6, SiteSettings.getInstance().getPlayerDefaultCredits());
            preparedStatement.setInt(7, SiteSettings.getInstance().getPlayerDefaultActivityPoints());
            preparedStatement.setInt(8, SiteSettings.getInstance().getPlayerDefaultVipPoints());
            preparedStatement.setInt(9, (int) (System.currentTimeMillis() / 1000l));
            preparedStatement.setInt(10, (int) (System.currentTimeMillis() / 1000l));
            preparedStatement.setInt(11, (int) (System.currentTimeMillis() / 1000l));

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                return new Player(resultSet.getInt(1), username, email);
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return null;
    }

    public static void createPreferences(int playerId, int homeRoom) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement(
                    "INSERT into player_settings (player_id, home_room) VALUES(?, ?);");

            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, homeRoom);

            preparedStatement.execute();
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }
    }

    public static void updateTicket(int id, String ticket) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("UPDATE players SET auth_ticket = ? WHERE id = ?");

            preparedStatement.setString(1, ticket);
            preparedStatement.setInt(2, id);

            preparedStatement.execute();
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }
    }

    public static PlayerPreferences getPreferences(int id) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT player_id, hide_online, allow_friend_requests, follow_friend_mode FROM player_settings WHERE player_id = ?");
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new PlayerPreferences(resultSet);
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return null;
    }

    public static void savePreferences(PlayerPreferences playerPreferences) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("UPDATE player_settings SET hide_online = ?, allow_friend_requests = ?, follow_friend_mode = ? WHERE player_id = ?");

            preparedStatement.setString(1, playerPreferences.showOnlineStatus() ? "1" : "0");
            preparedStatement.setString(2, playerPreferences.allowFriendRequests() ? "1" : "0");
            preparedStatement.setString(3, playerPreferences.getFollowMode().toString());
            preparedStatement.setInt(4, playerPreferences.getPlayerId());

            preparedStatement.execute();
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }
    }

    public static void save(Player player) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("UPDATE players SET username = ?, email = ?, figure = ?, gender = ?, motto = ?, credits = ?, activity_points = ?, rank = ?, password = ? WHERE id = ?");

            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setString(2, player.getEmail());
            preparedStatement.setString(3, player.getFigure());
            preparedStatement.setString(4, player.getGender());
            preparedStatement.setString(5, player.getMotto());
            preparedStatement.setInt(6, player.getCredits());
            preparedStatement.setInt(7, player.getActivityPoints());
            preparedStatement.setInt(8, player.getRank());
            preparedStatement.setString(9, player.getPassword());

            preparedStatement.setInt(10, player.getId());

            preparedStatement.execute();
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }
    }

    public static String getPlaylist(int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT playlist FROM player_settings WHERE player_id = ?");
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getString("playlist");
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return "[]";
    }

    public static String getUsernameById(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT username FROM players WHERE id = ?");
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return "UnknownUsername";
    }
}
