package com.cometproject.storage.mysql.repositories;

import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.data.StatementConsumer;
import com.cometproject.storage.mysql.data.results.IResultReader;
import com.cometproject.storage.mysql.data.results.ResultReaderConsumer;
import com.cometproject.storage.mysql.data.results.ResultSetReader;
import com.cometproject.storage.mysql.data.transactions.MySQLTransaction;
import com.cometproject.storage.mysql.data.transactions.Transaction;
import com.cometproject.storage.mysql.data.transactions.TransactionConsumer;
import org.apache.log4j.Logger;

import javax.validation.UnexpectedTypeException;
import java.sql.*;
import java.util.Arrays;
import java.util.function.Consumer;

public abstract class MySQLRepository {
    protected final Logger log = Logger.getLogger(MySQLRepository.class);

    private final MySQLConnectionProvider connectionProvider;

    public MySQLRepository(MySQLConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Executes a query and then for every result, resultConsumer is invoked with the provided ResultSet
     *
     * @param query          The query you'd like to run
     * @param resultConsumer Callback to be executed for every row returned by the query
     * @param parameters     Any parameters you'd like to bind to the prepared statement
     */
    public void select(String query, ResultReaderConsumer resultConsumer, Object... parameters) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(query);

            this.addParameters(preparedStatement, parameters);

            resultSet = preparedStatement.executeQuery();

            final IResultReader reader = new ResultSetReader(resultSet);

            while (resultSet.next()) {
                resultConsumer.accept(reader);
            }
        } catch (Exception e) {
            log.error("Failed to select data", e);
        } finally {
            this.connectionProvider.closeResults(resultSet);
            this.connectionProvider.closeStatement(preparedStatement);
            this.connectionProvider.closeConnection(connection);
        }
    }

    /**
     * Runs the update query with any parameters
     *
     * @param query      The query to run
     * @param parameters The parameters to bind in the query
     */
    public void update(String query, Object... parameters) {
        update(query, Transaction.NULL, parameters);
    }

    /**
     * Runs the update query with any parameters
     *
     * @param query       The query to run
     * @param transaction The transaction in which to execute the query within
     * @param parameters  The parameters to bind in the query
     */
    public void update(String query, Transaction transaction, Object... parameters) {
        Connection connection = transaction != null ? transaction.getConnection() : null;
        PreparedStatement preparedStatement = null;

        try {
            if (connection == null)
                connection = this.connectionProvider.getConnection();

            preparedStatement = connection.prepareStatement(query);

            this.addParameters(preparedStatement, parameters);

            // We could return or accept a consumer of the affected rows or something?
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            log.error("Failed to update data", e);
        } finally {
            this.connectionProvider.closeStatement(preparedStatement);

            if (transaction == null)
                this.connectionProvider.closeConnection(connection);
        }
    }

    /**
     * Runs update query as a batch (allows you to update multiple rows in 1 command)
     *
     * @param query       Query to run
     * @param transaction transaction (will run via this if provided)
     * @param params      Consumer to provide the statement with the params of the multiple rows
     *                    Note: use statement.addBatch() for every item in the batch
     */
    public void updateBatch(final String query, Transaction transaction, StatementConsumer params) {
        Connection connection = transaction != null ? transaction.getConnection() : null;
        PreparedStatement preparedStatement = null;

        try {
            if (connection == null)
                connection = this.connectionProvider.getConnection();

            preparedStatement = connection.prepareStatement(query);

            params.accept(preparedStatement);

            // We could return or accept a consumer of the affected rows or something?
            preparedStatement.executeBatch();
        } catch (Exception e) {
            log.error("Failed to update data", e);
        } finally {
            this.connectionProvider.closeStatement(preparedStatement);

            if (transaction == null)
                this.connectionProvider.closeConnection(connection);
        }
    }

    /**
     * Runs update query as a batch (allows you to update multiple rows in 1 command)
     *
     * @param query  Query to run
     * @param params Consumer to provide the statement with the params of the multiple rows
     *               Note: use statement.addBatch() for every item in the batch
     */
    public void updateBatch(final String query, StatementConsumer params) {
        updateBatch(query, null, params);
    }

    /**
     * Runs insert query as a batch (allows you to insert multiple rows in 1 command) and retrieve their primary key
     *
     * @param query       Query to run
     * @param params      Consumer to provide the statement with the params of the multiple rows
     *                    Note: use statement.addBatch() for every item in the batch
     * @param keyConsumer Consumer to retrieve the primary key of their respective inserted row
     */
    public void insertBatch(final String query, Transaction transaction, StatementConsumer params, ResultReaderConsumer keyConsumer) {
        Connection connection = transaction != null ? transaction.getConnection() : null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            if (connection == null)
                connection = this.connectionProvider.getConnection();

            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            params.accept(preparedStatement);

            preparedStatement.executeBatch();

            resultSet = preparedStatement.getGeneratedKeys();

            final IResultReader resultReader = new ResultSetReader(resultSet);

            while (resultSet.next()) {
                keyConsumer.accept(resultReader);
            }
        } catch (Exception e) {
            log.error("Failed to update data", e);
        } finally {
            this.connectionProvider.closeResults(resultSet);
            this.connectionProvider.closeStatement(preparedStatement);

            if (transaction == null)
                this.connectionProvider.closeConnection(connection);
        }
    }

    /**
     * Runs insert query as a batch (allows you to insert multiple rows in 1 command) and retrieve their primary key
     *
     * @param query       Query to run
     * @param params      Consumer to provide the statement with the params of the multiple rows
     *                    Note: use statement.addBatch() for every item in the batch
     * @param keyConsumer Consumer to retrieve the primary key of their respective inserted row
     */
    public void insertBatch(final String query, StatementConsumer params, ResultReaderConsumer keyConsumer) {
        insertBatch(query, null, params, keyConsumer);
    }

    /**
     * Runs the insert query and accepts a consumer for the new generated keys (if any)
     *
     * @param query       The query to execute
     * @param keyConsumer The consumer to accept the newly generated keys
     * @param parameters  The parameters to bind in the query
     */
    public void insert(String query, ResultReaderConsumer keyConsumer, Object... parameters) {
        insert(query, keyConsumer, Transaction.NULL, parameters);
    }

    /**
     * Runs the insert query with a provided transaction object (which allows rollback etc.)
     *
     * @param query       The query to execute
     * @param transaction The transaction in which to execute the query within;
     * @param keyConsumer The key consumer
     * @param parameters  The parameters to bind to the query
     */
    public void insert(String query, ResultReaderConsumer keyConsumer, Transaction transaction, Object... parameters) {
        Connection connection = transaction == Transaction.NULL ? null : transaction.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            if (connection == null) {
                connection = this.connectionProvider.getConnection();
            }

            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            this.addParameters(preparedStatement, parameters);

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();
            final IResultReader resultReader = new ResultSetReader(resultSet);

            if (resultSet.next()) {
                keyConsumer.accept(resultReader);
            }
        } catch (Exception e) {
            log.error("Failed to update data", e);
        } finally {
            this.connectionProvider.closeResults(resultSet);
            this.connectionProvider.closeStatement(preparedStatement);

            if (transaction == null) {
                this.connectionProvider.closeConnection(connection);
            }
        }
    }

    /**
     * Allows a MySQL connection to be shared throughout multiple queries and also support rollback etc
     *
     * @param transactionConsumer The consumer in which the transaction will be used
     */
    public void transaction(TransactionConsumer transactionConsumer) {
        Transaction transaction = null;

        try {
            transaction = new MySQLTransaction(this.connectionProvider.getConnection());

            // Start the transaction (configure the connection to not autoCommit)
            transaction.startTransaction();

            transactionConsumer.accept(transaction);
        } catch (Exception e) {
            try {
                if (transaction != null) {
                    transaction.rollback();

                    // TODO: make sure we perform any checks here for if the connection is closed, we can't have
                    //       connection leaks!
                }
            } catch (Exception ex) {
                log.error("Failed to rollback transaction", ex);
            }

            log.error("Failed to run transaction, rolling back", e);
        } finally {
            try {
                if (transaction != null)
                    transaction.getConnection().close();
            } catch (SQLException e) {
                log.error("Failed to close connection");
            }
        }
    }

    /**
     * Dynamically sets parameters to the prepared statement
     *
     * @param preparedStatement The statement of which to set the parameters
     * @param parameters        List of parameters defined as objects
     * @throws Exception Exception when setting the parameters
     */
    private void addParameters(PreparedStatement preparedStatement, Object... parameters) throws Exception {
        int parameterIndex = 1;
        for (Object obj : parameters) {
            if (obj instanceof Integer) {
                preparedStatement.setInt(parameterIndex++, (Integer) obj);
            } else if (obj instanceof String) {
                preparedStatement.setString(parameterIndex++, (String) obj);
            } else if (obj instanceof Long) {
                preparedStatement.setLong(parameterIndex++, (Long) obj);
            } else if (obj instanceof Boolean) {
                preparedStatement.setBoolean(parameterIndex++, (Boolean) obj);
            } else if (obj instanceof Double) {
                preparedStatement.setDouble(parameterIndex++, (Double) obj);
            } else {
                if (obj == null) {
                    preparedStatement.setString(parameterIndex++, null);
                    continue;
                }
                throw new UnexpectedTypeException("You can only bind types: Integer, String, Boolean, Long and Double to a statement!");
            }
        }
    }
}
