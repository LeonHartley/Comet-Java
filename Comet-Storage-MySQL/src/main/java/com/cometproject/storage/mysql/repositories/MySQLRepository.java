package com.cometproject.storage.mysql.repositories;

import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.data.IResultReader;
import com.cometproject.storage.mysql.data.ResultReaderConsumer;
import com.cometproject.storage.mysql.data.ResultSetReader;
import org.apache.log4j.Logger;

import javax.validation.UnexpectedTypeException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class MySQLRepository {
    private final Logger log = Logger.getLogger(MySQLRepository.class);

    private final MySQLConnectionProvider connectionProvider;

    public MySQLRepository(MySQLConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Executes a query and then for every result, resultConsumer is invoked with the provided ResultSet
     * @param query The query you'd like to run
     * @param resultConsumer Callback to be executed for every row returned by the query
     * @param parameters Any parameters you'd like to bind to the prepared statement
     */
    public void select(String query, ResultReaderConsumer resultConsumer, Object... parameters) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        try {
            connection = this.connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(query);

            this.addParameters(preparedStatement, parameters);

            resultSet = preparedStatement.executeQuery();

            final IResultReader reader = new ResultSetReader(resultSet);

            while(resultSet.next()) {
                resultConsumer.accept(reader);
            }
        } catch (Exception e) {
            log.error("Failed to select data", e);
        } finally {
            this.connectionProvider.closeConnection(connection);
            this.connectionProvider.closeStatement(preparedStatement);
        }
    }

    public void update(String query, Object... parameters) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.connectionProvider.getConnection();

            preparedStatement = connection.prepareStatement(query);

            this.addParameters(preparedStatement, parameters);

            preparedStatement.execute();
        } catch (Exception e) {
            log.error("Failed to update data", e);
        } finally {
            this.connectionProvider.closeStatement(preparedStatement);
            this.connectionProvider.closeConnection(connection);
        }
    }

    public void insert(String query, ResultReaderConsumer keyConsumer, Object... parameters) {
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        try {
            connection = this.connectionProvider.getConnection();

            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            this.addParameters(preparedStatement, parameters);

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();
            final IResultReader resultReader = new ResultSetReader(resultSet);

            if(resultSet.next()) {
                keyConsumer.accept(resultReader);
            }
            preparedStatement.execute();
        } catch (Exception e) {
            log.error("Failed to update data", e);
        }
    }

    /**
     * Dynamically sets parameters to the prepared statement
     * @param preparedStatement The statement of which to set the parameters
     * @param parameters List of parameters defined as objects
     * @throws Exception Exception when setting the parameters
     */
    private void addParameters(PreparedStatement preparedStatement, Object... parameters) throws Exception {
        int parameterIndex = 1;
        for (Object obj : parameters) {
            if(obj instanceof Integer) {
                preparedStatement.setInt(parameterIndex++, (Integer) obj);
            } else if(obj instanceof String) {
                preparedStatement.setString(parameterIndex++, (String) obj);
            } else if(obj instanceof Long) {
                preparedStatement.setLong(parameterIndex++, (Long) obj);
            } else {
                throw new UnexpectedTypeException("You can only bind types: Integer, String and Long to a statement!");
            }
        }
    }
}
