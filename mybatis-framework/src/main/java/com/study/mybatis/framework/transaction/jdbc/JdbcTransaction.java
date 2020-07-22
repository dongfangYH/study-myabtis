package com.study.mybatis.framework.transaction.jdbc;

import com.study.mybatis.framework.transaction.Transaction;
import com.study.mybatis.framework.transaction.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {

    protected Connection connection;
    protected DataSource dataSource;
    protected TransactionIsolationLevel level;
    protected boolean autoCommit;

    public JdbcTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        this.dataSource = dataSource;
        this.level = level;
        this.autoCommit = autoCommit;
    }

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            openConnection();
        }
        return connection;
    }

    protected void openConnection() throws SQLException {
        connection = dataSource.getConnection();
        if (level != null) {
            connection.setTransactionIsolation(level.getLevel());
        }
        setDesiredAutoCommit(autoCommit);
    }

    protected void setDesiredAutoCommit(boolean desiredAutoCommit) {
        try {
            if (connection.getAutoCommit() != desiredAutoCommit) {
                connection.setAutoCommit(desiredAutoCommit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error configuring AutoCommit.  "
                    + "Your driver may not support getAutoCommit() or setAutoCommit(). "
                    + "Requested setting: " + desiredAutoCommit + ".  Cause: " + e, e);
        }
    }

    @Override
    public void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            resetAutoCommit();
            connection.close();
        }
    }

    protected void resetAutoCommit() {
        try {
            if (!connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return null;
    }
}
