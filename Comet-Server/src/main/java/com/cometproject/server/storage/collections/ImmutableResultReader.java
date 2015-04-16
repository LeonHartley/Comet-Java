package com.cometproject.server.storage.collections;

import com.cometproject.server.storage.SqlHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ImmutableResultReader {

    private final ResultSet resultSet;
    private final List<HashMap<String, Object>> dataStore = new ArrayList<>();

    public static ImmutableResultReader build(ResultSet resultSet) {
        return new ImmutableResultReader(resultSet);
    }

    public static EmptyImmutableResultReader empty() {
        return new EmptyImmutableResultReader();
    }

    public ImmutableResultReader(ResultSet resultSet) {
        this.resultSet = resultSet;
        this.init(true);
    }

    public ImmutableResultReader(ResultSet resultSet, boolean shouldCloseRs) {
        this.resultSet = resultSet;
        this.init(shouldCloseRs);
    }

    protected void init(boolean shouldCloseRs) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                int rowNumber = 0;

                while (resultSet.next()) {
                    rowNumber++;

                    if (dataStore.get(rowNumber) == null) {
                        dataStore.add(new HashMap<String, Object>());
                    }

                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        dataStore.get(rowNumber).put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                    }
                }
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            if (shouldCloseRs) {
                try {
                    if (this.resultSet != null)
                        this.resultSet.close();
                } catch (SQLException e) {
                    SqlHelper.handleSqlException(e);
                }
            }
        }
    }

    public int size() {
        return this.dataStore.size();
    }

    public HashMap<String, Object> getRow(int row) {
        return this.dataStore.get(row);
    }

    public int getInt(String columnName, int row) {
        if (this.getRow(row).containsKey(columnName)) {
            return (int) this.getRow(row).get(columnName);
        }

        return 0;
    }

    public String getString(String columnName, int row) {
        if (this.getRow(row).containsKey(columnName)) {
            return (String) this.getRow(row).get(columnName);
        }

        return null;
    }

    public double getDouble(String columnName, int row) {
        if (this.getRow(row).containsKey(columnName)) {
            return (double) this.getRow(row).get(columnName);
        }

        return 0d;
    }

    public boolean getBoolean(String columnName, int row) {
        return this.getRow(row).containsKey(columnName) && (this.getRow(row).get(columnName)).equals("1");
    }
}
