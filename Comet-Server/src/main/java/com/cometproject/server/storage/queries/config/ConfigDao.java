package com.cometproject.server.storage.queries.config;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.rooms.filter.FilterMode;
import com.cometproject.server.storage.SqlHelper;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class ConfigDao {
    public static void getAll() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet config = null;
        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM server_configuration LIMIT 1", sqlConnection);

            config = preparedStatement.executeQuery();

            while(config.next()) {

                CometSettings.messageOfTheDayEnabled = config.getBoolean("motd_enabled");
                CometSettings.messageOfTheDayText = config.getString("motd_message");
                CometSettings.hotelName = config.getString("hotel_name");
                CometSettings.hotelUrl = config.getString("hotel_url");
                CometSettings.groupCost = config.getInt("group_cost");
                CometSettings.onlineRewardEnabled = config.getBoolean("online_reward_enabled");
                CometSettings.onlineRewardCredits = config.getInt("online_reward_credits");
                CometSettings.onlineRewardDuckets = config.getInt("online_reward_duckets");
                CometSettings.onlineRewardInterval = config.getInt("online_reward_interval");
                CometSettings.aboutImg = config.getString("about_image");
                CometSettings.showUsersOnlineInAbout = config.getBoolean("about_show_players_online");
                CometSettings.showActiveRoomsInAbout = config.getBoolean("about_show_rooms_active");
                CometSettings.showUptimeInAbout = config.getBoolean("about_show_uptime");
                CometSettings.floorMaxX = config.getInt("floor_editor_max_x");
                CometSettings.floorMaxY = config.getInt("floor_editor_max_y");
                CometSettings.floorMaxTotal = config.getInt("floor_editor_max_total");
                CometSettings.maxPlayersInRoom = config.getInt("room_max_players");
                CometSettings.roomPasswordEncryptionEnabled = config.getBoolean("room_encrypt_passwords");
                CometSettings.placeItemOnEntity = config.getBoolean("room_can_place_item_on_entity");
                CometSettings.maxBotsInRoom = config.getInt("room_max_bots");
                CometSettings.maxPetsInRoom = config.getInt("room_max_pets");
                CometSettings.wiredRewardMinRank = config.getInt("room_wired_reward_minimum_rank");
                CometSettings.idleMinutes = config.getInt("room_idle_minutes");
                CometSettings.wordFilterMode = FilterMode.valueOf(config.getString("word_filter_mode").toUpperCase());
                CometSettings.useDatabaseIp = config.getBoolean("use_database_ip");
                CometSettings.storeAccess = config.getBoolean("save_logins");
                CometSettings.infiniteBalance = config.getBoolean("player_infinite_balance");
                CometSettings.giftCooldown = config.getInt("player_gift_cooldown");
                CometSettings.playerFigureUpdateTimeout = config.getInt("player_change_figure_cooldown");
                CometSettings.figureValidation = config.getBoolean("player_figure_validation");
                CometSettings.maxFriends = config.getInt("messenger_max_friends");
                CometSettings.logMessengerMessages = config.getBoolean("messenger_log_messages");
                CometSettings.itemStorageQueueEnabled = config.getBoolean("storage_item_queue_enabled");
                CometSettings.itemStorageQueueEnabled = config.getBoolean("storage_player_queue_enabled");

                final String characters = config.getString("word_filter_strict_chars");

                CometSettings.strictFilterCharacters.clear();

                for (String charSet : characters.split(",")) {
                    if (!charSet.contains(":")) continue;

                    final String[] chars = charSet.split(":");

                    if (chars.length == 2) {
                        CometSettings.strictFilterCharacters.put(chars[0], chars[1]);
                    } else {
                        CometSettings.strictFilterCharacters.put(chars[0], "");
                    }
                }
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(config);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
