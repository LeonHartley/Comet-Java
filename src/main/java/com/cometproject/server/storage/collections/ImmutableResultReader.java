package com.cometproject.server.storage.collections;

import com.cometproject.server.storage.SqlHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matty on 30/04/2014.
 */
public class ImmutableResultReader {

    private final ResultSet resultSet;
    private final Map<String, Object> dataStore = new HashMap<>();

    public ImmutableResultReader(ResultSet resultSet, boolean shouldCloseRs) {
        this.resultSet = resultSet;

        try {
            if (resultSet != null && !resultSet.isClosed()) {
                int curr = 0;
                while (resultSet.next()) {
                    curr++;

                    System.out.println(resultSet.getMetaData().getColumnName(curr));

                    dataStore.put(resultSet.getMetaData().getColumnName(curr), resultSet.getObject(curr));
                }
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            if (shouldCloseRs) {
                try {
                    this.resultSet.close();
                } catch (SQLException e) { }
            }
        }
    }

    public int size() {
        return this.dataStore.size();
    }

    public Object getObject(String columnName) {
        return this.dataStore.get(columnName);
    }

    public int getInt(String columnName) {
        return (int)this.getObject(columnName);
    }
}
