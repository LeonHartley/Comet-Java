package com.cometproject.storage.mysql.data;

import java.sql.ResultSet;

public class ResultSetReader implements IResultReader {

    private final ResultSet resultSet;

    public ResultSetReader(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public String readString(String columnName) throws Exception {
        return this.resultSet.getString(columnName);
    }

    @Override
    public int readInteger(String columnName) throws Exception {
        return this.resultSet.getInt(columnName);
    }

    @Override
    public long readLong(String columnName) throws Exception {
        return this.resultSet.getLong(columnName);
    }

    @Override
    public boolean readBoolean(String columnName) throws Exception {
        return this.resultSet.getBoolean(columnName);
    }
}
