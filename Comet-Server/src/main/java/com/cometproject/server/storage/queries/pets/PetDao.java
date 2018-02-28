package com.cometproject.server.storage.queries.pets;

import com.cometproject.api.game.pets.IPetData;
import com.cometproject.api.game.pets.IPetStats;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.pets.data.PetMessageType;
import com.cometproject.server.game.pets.data.PetSpeech;
import com.cometproject.server.game.pets.data.StaticPetProperties;
import com.cometproject.server.game.pets.races.PetBreedLevel;
import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.storage.SqlHelper;
import com.google.common.collect.Lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
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

                if (!data.containsKey(petType)) {
                    data.put(petType, new PetSpeech());
                }

                PetSpeech petSpeech = data.get(petType);

                if (!petSpeech.getMessages().containsKey(messageType)) {
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

    public static Map<String, String> getTransformablePets() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<String, String> data = new ConcurrentHashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM pet_transformable", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getString("name"), resultSet.getString("data"));
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

    public static Map<Integer, IPetData> getPetsByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, IPetData> data = new ConcurrentHashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT " +
                    "pet.`id` AS id, " +
                    "pet.`pet_name` AS pet_name, " +
                    "pet.`level` AS `level`, " +
                    "pet.`happiness` AS happiness, " +
                    "pet.`experience` AS experience, " +
                    "pet.`energy` AS energy, " +
                    "pet.`scratches` AS scratches, " +
                    "pet.`hunger` AS hunger, " +
                    "pet.`owner_id` AS owner_id, " +
                    "pet.`colour` AS colour, " +
                    "pet.`race_id` AS race_id, " +
                    "pet.`type` AS `type`, " +
                    "pet.`saddled` AS saddled, " +
                    "pet.`hair_style` AS hair_style, " +
                    "pet.`hair_colour` AS hair_colour, " +
                    "pet.`any_rider` AS any_rider, " +
                    "pet.`birthday` AS birthday, " +
                    "pet.`x` AS `x`,  " +
                    "pet.`y` AS `y`, " +
                    "player.username AS owner_name " +
                    " FROM pet_data AS pet  " +
                    " RIGHT JOIN `players` AS player ON player.id = pet.owner_id " +
                    " WHERE pet.owner_id = ? AND pet.room_id = 0", sqlConnection);

            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final int id = resultSet.getInt("id");
                final String name = resultSet.getString("pet_name");
                final int level = resultSet.getInt("level");
                final int scratches = resultSet.getInt("scratches");
                final int happiness = resultSet.getInt("happiness");
                final int experience = resultSet.getInt("experience");
                final int energy = resultSet.getInt("energy");
                final int hunger = resultSet.getInt("hunger");
                final int ownerId = resultSet.getInt("owner_id");
                final String ownerName = resultSet.getString("owner_name");
                final String colour = resultSet.getString("colour");
                final int raceId = resultSet.getInt("race_id");
                final int typeId = resultSet.getInt("type");
                final boolean saddled = resultSet.getBoolean("saddled");
                final int hairDye = resultSet.getInt("hair_colour");
                final int hair = resultSet.getInt("hair_style");
                final boolean anyRider = resultSet.getBoolean("any_rider");
                final int birthday = resultSet.getInt("birthday");

                final Position position = new Position(resultSet.getInt("x"), resultSet.getInt("y"));
                data.put(id, new PetData(id, name, scratches, level, happiness, experience, energy, hunger, ownerId,
                        ownerName, colour, raceId, typeId, hairDye, hair, anyRider, saddled, birthday, position));
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

            preparedStatement = SqlHelper.prepare("INSERT INTO `pet_data` (`owner_id`, `pet_name`, `type`, `race_id`, `colour`, `scratches`, `level`, `happiness`, `experience`, `energy`, `birthday`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", sqlConnection, true);

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
            preparedStatement.setInt(11, (int) Comet.getTime());

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

    public static void saveStats(int scratches, int level, int happiness, int experience, int energy, int hunger, int petId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE pet_data SET scratches = ?, level = ?, happiness = ?, experience = ?, energy = ?, hunger = ? WHERE id = ?", sqlConnection);

            preparedStatement.setInt(1, scratches);
            preparedStatement.setInt(2, level);
            preparedStatement.setInt(3, happiness);
            preparedStatement.setInt(4, experience);
            preparedStatement.setInt(5, energy);
            preparedStatement.setInt(6, hunger);

            preparedStatement.setInt(7, petId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }


    public static void saveStatsBatch(final Set<IPetStats> petStats) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE pet_data SET scratches = ?, level = ?, happiness = ?, experience = ?, energy = ?, hunger = ? WHERE id = ?;", sqlConnection);

            for (IPetStats pet : petStats) {
                preparedStatement.setInt(1, pet.getScratches());
                preparedStatement.setInt(2, pet.getLevel());
                preparedStatement.setInt(3, pet.getHappiness());
                preparedStatement.setInt(4, pet.getExperience());
                preparedStatement.setInt(5, pet.getEnergy());
                preparedStatement.setInt(6, pet.getHunger());

                preparedStatement.setInt(7, pet.getId());

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

    public static void saveHorseData(int id, boolean saddled, int hair, int hairDye, boolean anyRider, int raceId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE pet_data SET saddled = ?, hair_style = ?, hair_colour = ?, any_rider = ?, race_id = ? WHERE id = ?", sqlConnection);

            preparedStatement.setString(1, saddled ? "true" : "false");
            preparedStatement.setInt(2, hair);
            preparedStatement.setInt(3, hairDye);
            preparedStatement.setString(4, anyRider ? "true" : "false");
            preparedStatement.setInt(5, raceId);

            preparedStatement.setInt(6, id);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static Map<Integer, Map<PetBreedLevel, Set<Integer>>> getPetBreedPallets() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, Map<PetBreedLevel, Set<Integer>>> data = new ConcurrentHashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM pet_breeds;", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final int petType = resultSet.getInt("pet_type");
                final int palletId = resultSet.getInt("pallet_id");
                final PetBreedLevel breedLevel = PetBreedLevel.valueOf(resultSet.getString("level"));

                if (!data.containsKey(petType)) {
                    data.put(petType, new ConcurrentHashMap<PetBreedLevel, Set<Integer>>() {{
                        put(PetBreedLevel.EPIC, new HashSet<>());
                        put(PetBreedLevel.RARE, new HashSet<>());
                        put(PetBreedLevel.UNCOMMON, new HashSet<>());
                        put(PetBreedLevel.COMMON, new HashSet<>());
                    }});
                }

                data.get(petType).get(breedLevel).add(palletId);
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
}
