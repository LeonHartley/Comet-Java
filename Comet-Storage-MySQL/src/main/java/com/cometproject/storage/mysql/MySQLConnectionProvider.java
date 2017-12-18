package com.cometproject.storage.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class MySQLConnectionProvider {

    public abstract Connection getConnection() throws Exception;

    public abstract void closeConnection(Connection connection);

    public abstract void closeStatement(PreparedStatement statement);

    public abstract void closeResults(ResultSet resultSet);

}

