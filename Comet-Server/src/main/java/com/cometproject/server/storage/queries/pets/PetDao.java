package com.cometproject.server.storage.queries.pets;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.pets.data.PetSpeech;
import com.cometproject.server.game.pets.data.PetMessageType;
import com.cometproject.server.game.pets.data.StaticPetProperties;
import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.storage.SqlHelper;
import com.google.common.collect.Lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class PetDao {
    public static List<PetRace> getRaces() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<PetRace> data = new ArrayList<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM pet_races", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(new PetRace(resultSet));
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

    public static Map<Integer, PetSpeech> getMessages(AtomicInteger petSpeechCount) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, PetSpeech> data = new ConcurrentHashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM pet_messages", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int petType = resultSet.getInt("pet_type");
                PetMessageType messageType = PetMessageType.valueOf(resultSet.getString("message_type"));

                if(!data.containsKey(petType)) {
                    data.put(petType, new PetSpeech());
                }

                PetSpeech petSpeech = data.get(petType);

                if(!petSpeech.getMessages().containsKey(messageType)) {
                    petSpeech.getMessages().put(messageType, Lists.newArrayList());
                }

                petSpeechCount.incrementAndGet();
                petSpeech.getMessages().get(messageType).add(resultSet.getString("message_string"));
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

    public static Map<Integer, PetData> getPetsByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, PetData> data = new HashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM pet_data WHERE owner_id = ? AND room_id = 0", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getInt("id"), new PetData(resultSet));
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

    public static int createPet(int ownerId, String petName, int type, int race, String colour) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT INTO `pet_data` (`owner_id`, `pet_name`, `type`, `race_id`, `colour`, `scratches`, `level`, `happiness`, `experience`, `energy`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", sqlConnection, true);

            preparedStatement.setInt(1, ownerId);
            preparedStatement.setString(2, petName);
            preparedStatement.setInt(3, type);
            preparedStatement.setInt(4, race);
            preparedStatement.setString(5, colour);
            preparedStatement.setInt(6, 0);
            preparedStatement.setInt(7, StaticPetProperties.DEFAULT_LEVEL);
            preparedStatement.setInt(8, StaticPetProperties.DEFAULT_HAPPINESS);
            preparedStatement.setInt(9, StaticPetProperties.DEFAULT_EXPERIENCE);
            preparedStatement.setInt(10, StaticPetProperties.DEFAULT_ENERGY);

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

    public static void savePosition(int x, int y, int id) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE pet_data SET x = ?, y = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setInt(3, id);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveStats(int scratches, int level, int happiness, int experience, int energy, int petId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE pet_data SET scratches = ?, level = ?, happiness = ?, experience = ?, energy = ? WHERE id = ?", sqlConnection);

            preparedStatement.setInt(1, scratches);
            preparedStatement.setInt(2, level);
            preparedStatement.setInt(3, happiness);
            preparedStatement.setInt(4, experience);
            preparedStatement.setInt(5, energy);

            preparedStatement.setInt(6, petId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void deletePets(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("DELETE FROM pet_data WHERE owner_id = ? AND room_id = 0;", sqlConnection);
            preparedStatement.setInt(1, playerId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
