package com.cometproject.server.storage.queries.pets;

import com.cometproject.api.game.pets.IPetData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RoomPetDao {
    public static List<IPetData> getPetsByRoomId(int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<IPetData> data = new ArrayList<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT " +
                    "pet.`id` AS id, " +
                    "pet.`pet_name` AS pet_name, " +
                    "pet.`level` AS `level`, " +
                    "pet.`happiness` AS happiness, " +
                    "pet.`experience` AS experience, " +
                    "pet.`energy` AS energy, " +
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
                    "pet.`scratches` AS scratches, " +
                    "pet.`x` AS `x`,  " +
                    "pet.`y` AS `y`, " +
                    "player.username AS owner_name " +
                    " FROM pet_data AS pet  " +
                    " RIGHT JOIN `players` AS player ON player.id = pet.owner_id " +
                    " WHERE pet.room_id = ?", sqlConnection);

            preparedStatement.setInt(1, roomId);

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

                data.add(new PetData(id, name, scratches, level, happiness, experience, energy, hunger, ownerId,
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

    public static void updatePet(int roomId, int x, int y, int petId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE pet_data SET room_id = ?, x = ?, y = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, roomId);
            preparedStatement.setInt(2, x);
            preparedStatement.setInt(3, y);
            preparedStatement.setInt(4, petId);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
