package com.study.mybatis.framework.statement;

import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.parameter.ParameterHandler;
import com.study.mybatis.framework.resultset.ResultSetHandler;
import com.study.mybatis.framework.session.Configuration;
import com.study.mybatis.framework.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseStatementHandler implements StatementHandler{

    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;
    protected final ResultSetHandler resultSetHandler;
    protected final ParameterHandler parameterHandler;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement,
                                ResultHandler resultHandler, Object parameterObject) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject);
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler);
    }

    @Override
    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);
            setStatementTimeout(statement, transactionTimeout);
            //setFetchSize(statement);
            return statement;
        } catch (SQLException e) {
            closeStatement(statement);
            throw e;
        } catch (Exception e) {
            closeStatement(statement);
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;

    protected void setStatementTimeout(Statement stmt, Integer transactionTimeout) throws SQLException {
        Integer queryTimeout = null;
        if (mappedStatement.getTimeout() != null) {
            queryTimeout = mappedStatement.getTimeout();
        } else if (configuration.getDefaultStatementTimeout() != null) {
            queryTimeout = configuration.getDefaultStatementTimeout();
        }
        if (queryTimeout != null) {
            stmt.setQueryTimeout(queryTimeout);
        }
        StatementUtil.applyTransactionTimeout(stmt, queryTimeout, transactionTimeout);
    }

    protected void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            //ignore
        }
    }
}
