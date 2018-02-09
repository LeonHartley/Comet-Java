package com.cometproject.storage.mysql.data.results;

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
    public String readString(int index) throws Exception {
        return this.resultSet.getString(index);
    }

    @Override
    public int readInteger(String columnName) throws Exception {
        return this.resultSet.getInt(columnName);
    }

    @Override
    public int readInteger(int index) throws Exception {
        return this.resultSet.getInt(index);
    }

    @Override
    public long readLong(String columnName) throws Exception {
        return this.resultSet.getLong(columnName);
    }

    @Override
    public long readLong(int index) throws Exception {
        return this.resultSet.getLong(index);
    }

    @Override
    public boolean readBoolean(String columnName) throws Exception {
        return this.resultSet.getBoolean(columnName);
    }

    @Override
    public boolean readBoolean(int index) throws Exception {
        return this.resultSet.getBoolean(index);
    }

    @Override
    public double readDouble(String columnName) throws Exception {
        return this.resultSet.getDouble(columnName);
    }

    @Override
    public double readDouble(int index) throws Exception {
        return this.resultSet.getDouble(index);
    }
}
