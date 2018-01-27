package com.cometproject.storage.mysql.data.results;

public interface IResultReader {
    String readString(String columnName) throws Exception;

    String readString(int index) throws Exception;

    int readInteger(String columnName) throws Exception;

    int readInteger(int index) throws Exception;

    long readLong(String columnName) throws Exception;

    long readLong(int index) throws Exception;

    boolean readBoolean(String columnName) throws Exception;

    boolean readBoolean(int index) throws Exception;

    double readDouble(String columnName) throws Exception;

    double readDouble(int index) throws Exception;

}
