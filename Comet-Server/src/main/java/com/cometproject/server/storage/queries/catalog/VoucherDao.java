package com.cometproject.server.storage.queries.catalog;

import com.cometproject.api.game.catalog.types.vouchers.VoucherStatus;
import com.cometproject.api.game.catalog.types.vouchers.VoucherType;
import com.cometproject.server.game.catalog.types.Voucher;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoucherDao {
    public static Voucher findVoucherByCode(final String code) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Voucher voucher = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM `vouchers` WHERE `code` = ?", sqlConnection);

            preparedStatement.setString(1, code);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                voucher = new Voucher(resultSet.getInt("id"), VoucherType.valueOf(resultSet.getString("type")),
                        resultSet.getString("data"), resultSet.getInt("created_by"), resultSet.getInt("created_at"),
                        resultSet.getInt("claimed_by"), resultSet.getInt("claimed_at"),
                        VoucherStatus.valueOf(resultSet.getString("status")), resultSet.getString("code"));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return voucher;
    }

    public static void claimVoucher(final int voucherId, final int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE `vouchers` SET `status` = 'CLAIMED', `claimed_by` = ?, `claimed_at` = UNIX_TIMESTAMP() WHERE id = ?;", sqlConnection);

            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, voucherId);

            preparedStatement.execute();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
