package com.cometproject.server.storage.queries.pets;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RoomPetDao {
    public static List<PetData> getPetsByRoomId(int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<PetData> data = new ArrayList<>();

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
                    "pet.`x` AS `x`,  " +
                    "pet.`y` AS `y`, " +
                    "player.username AS owner_name " +
                    " FROM pet_data AS pet  " +
                    " RIGHT JOIN `players` AS player ON player.id = pet.owner_id " +
                    " WHERE pet.room_id = ?", sqlConnection);

            preparedStatement.setInt(1, roomId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PetData petData = new PetData(resultSet);
                petData.setRoomPosition(new Position(resultSet.getInt("x"), resultSet.getInt("y")));

                data.add(petData);
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
